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
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.request.BookingStatusUpdateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.mapper.BookingMapper;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.RoomRepository;
import uzumtech.jbooking.service.BookingService;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    RoomRepository roomRepository;

    @Override
    @Transactional
    public BookingResponse create(BookingCreateRequest request) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("Room Not Found"));

        if (!bookingRepository.isRoomAvailable(request.roomId(), request.checkIn(), request.checkOut())) {
            throw new IllegalStateException("Room is already  occupied");
        }

        if(request.guests().size() > room.getCapacity()) {
            throw new IllegalStateException("Guest count exceeds room capacity");
        }

        Booking booking = bookingMapper.toEntity(request);
        booking.setRoom(room);
        booking.setBookingStatus(BookingStatus.HOLD);

        return bookingMapper.toBookingResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse getById(Long id) {


    }
}