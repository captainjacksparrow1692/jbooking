package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.BookingMapper;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.RoomRepository;
import uzumtech.jbooking.service.BookingService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    RoomRepository roomRepository;
    BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse create(BookingCreateRequest request) {

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        boolean available = bookingRepository.isRoomAvailable(
                request.roomId(),
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
        booking.setHoldUntil(LocalDateTime.now().plusMinutes(15));

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toBookingResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository
                .findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public void cancelMyBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository
                .findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
    }
}
