package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.dto.BookingCreatedEvent;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.BookingMapper;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.RoomRepository;
import uzumtech.jbooking.service.BookingService;
import uzumtech.jbooking.service.KafkaProducerService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    RoomRepository roomRepository;
    BookingMapper bookingMapper;
    KafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public BookingResponse create(BookingCreateRequest request) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        return createBookingForRoom(request, room);
    }

    private BookingResponse createBookingForRoom(BookingCreateRequest request, Room room) {
        boolean available = bookingRepository.isRoomAvailable(
                room.getId(),
                request.checkInDate(),
                request.checkOutDate()
        );
        if (!available) {
            throw new IllegalStateException("Room is not available for selected dates");
        }

        Booking booking = bookingMapper.toEntity(request);
        booking.setRoom(room);
        booking.setBookingStatus(BookingStatus.HOLD);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setHoldUntil(LocalDateTime.now().plusMinutes(Constant.DEFAULT_BOOKING_HOLD_MINUTES));

        Booking saved = bookingRepository.save(booking);

        kafkaProducerService.sendBookingCreated(new BookingCreatedEvent(
                saved.getId(),
                saved.getRoom().getId(),
                request.userId(),
                saved.getCheckInDate(),
                saved.getCheckOutDate(),
                saved.getGuestsCount(),
                saved.getCreatedAt()
        ));

        return bookingMapper.toBookingResponse(saved);
    }

    @Override
    public BookingResponse getById(UUID userId, UUID bookingId) {
        Booking booking = bookingRepository
                .findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public void cancelMyBooking(UUID userId, UUID bookingId) {
        Booking booking = bookingRepository
                .findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
    }
}
