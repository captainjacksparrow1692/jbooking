package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.HistoryActionType;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.request.BookingStatusUpdateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.BookingHistory;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.BookingMapper;
import uzumtech.jbooking.repository.BookingHistoryRepository;
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
    BookingHistoryRepository bookingHistoryRepository;

    @Override
    @Transactional
    public BookingResponse create(BookingCreateRequest request) {
        if (!bookingRepository.isRoomAvailable(request.roomId(), request.checkIn(), request.checkOut())) {
            throw new RuntimeException("Room is occupied for these dates");
        }

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Booking booking = bookingMapper.toEntity(request);
        booking.setRoom(room);
        booking.setBookingStatus(BookingStatus.HOLD);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setHoldUntil(LocalDateTime.now().plusMinutes(15));

        Booking savedBooking = bookingRepository.save(booking);

        logAction(savedBooking, HistoryActionType.CREATE, "Booking created. Awaiting payment (15 min hold).");

        return bookingMapper.toBookingResponse(savedBooking);
    }

    @Override
    public BookingResponse getById(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toBookingResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    @Transactional
    public void updateStatus(BookingStatusUpdateRequest request) {
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setBookingStatus(request.bookingStatus());
        bookingRepository.save(booking);

        logAction(booking, HistoryActionType.UPDATE, "Booking status changed to: " + request.bookingStatus());
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        logAction(booking, HistoryActionType.CANCEL, "Booking has been cancelled.");
    }

    @Override
    public Page<BookingResponse> getAll(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(bookingMapper::toBookingResponse);
    }

    @Override
    public Page<BookingResponse> getByUserId(Long userId, Pageable pageable) {
        return bookingRepository.findByUserId(userId, pageable).map(bookingMapper::toBookingResponse);
    }

    private void logAction(Booking booking, HistoryActionType historyActionType, String details) {
        BookingHistory history = new BookingHistory();
        history.setBooking(booking);
        history.setHistoryActionType(historyActionType);
        history.setBookingStatus(booking.getBookingStatus()); // Save current status for history
        history.setActionTimestamp(LocalDateTime.now());
        history.setDetails(details);

        bookingHistoryRepository.save(history);
    }
}