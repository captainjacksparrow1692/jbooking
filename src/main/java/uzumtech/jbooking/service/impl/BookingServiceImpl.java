package uzumtech.jbooking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.request.BookingStatusUpdateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Room;
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
        if (!bookingRepository.isRoomAvailable(request.roomId(), request.checkIn(), request.checkOut())) {
            throw new RuntimeException("Номер занят на эти даты");
        }

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new EntityNotFoundException("Комната не найдена"));

        Booking booking = bookingMapper.toEntity(request);
        booking.setRoom(room);
        booking.setBookingStatus(BookingStatus.HOLD);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setHoldUntil(LocalDateTime.now().plusMinutes(15));

        return bookingMapper.toBookingResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse getById(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toBookingResponse)
                .orElseThrow(() -> new EntityNotFoundException("Бронь не найдена"));
    }

    @Override
    @Transactional
    public void updateStatus(BookingStatusUpdateRequest request) {
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Бронь не найдена"));
        booking.setBookingStatus(request.bookingStatus());
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронь не найдена"));
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public Page<BookingResponse> getAll(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(bookingMapper::toBookingResponse);
    }

    @Override
    public Page<BookingResponse> getByUserId(Long userId, Pageable pageable) {
        return bookingRepository.findByUserId(userId, pageable).map(bookingMapper::toBookingResponse);
    }
}