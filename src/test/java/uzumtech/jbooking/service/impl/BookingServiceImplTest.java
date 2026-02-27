package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Hotel;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.BookingMapper;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.RoomRepository;
import uzumtech.jbooking.service.KafkaProducerService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    BookingMapper bookingMapper;

    @Mock
    KafkaProducerService kafkaProducerService;

    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void create_shouldCreateBookingAndSendKafkaEvent() {
        Long roomId = 1L;
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        BookingCreateRequest request = new BookingCreateRequest(
                null, 10L, roomId, checkIn, checkOut, 2, null
        );

        Room room = new Room();
        room.setId(roomId);
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        room.setHotel(hotel);

        Booking mappedEntity = new Booking();
        Booking savedBooking = new Booking();
        savedBooking.setId(100L);
        savedBooking.setRoom(room);
        savedBooking.setCheckInDate(checkIn);
        savedBooking.setCheckOutDate(checkOut);
        savedBooking.setGuestsCount(2);
        savedBooking.setBookingStatus(BookingStatus.HOLD);
        savedBooking.setCreatedAt(LocalDateTime.now());

        BookingResponse expectedResponse = new BookingResponse(
                100L, "Test Hotel", null, roomId, null, null,
                checkIn, checkOut, null, BookingStatus.HOLD, null,
                savedBooking.getCreatedAt(), null
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(bookingRepository.isRoomAvailable(eq(roomId), eq(checkIn), eq(checkOut))).thenReturn(true);
        when(bookingMapper.toEntity(request)).thenReturn(mappedEntity);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toBookingResponse(savedBooking)).thenReturn(expectedResponse);

        BookingResponse result = bookingService.create(request);

        assertThat(result).isEqualTo(expectedResponse);
        assertThat(result.bookingId()).isEqualTo(100L);
        verify(kafkaProducerService).sendBookingCreated(any(BookingCreateRequest.class));
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void create_shouldThrowWhenRoomNotFound() {
        BookingCreateRequest request = new BookingCreateRequest(
                null, 10L, 999L, LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3), 2, null
        );

        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Room not found");
    }

    @Test
    void create_shouldThrowWhenRoomNotAvailable() {
        Long roomId = 1L;
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        BookingCreateRequest request = new BookingCreateRequest(
                null, 10L, roomId, checkIn, checkOut, 2, null
        );

        Room room = new Room();
        room.setId(roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(bookingRepository.isRoomAvailable(roomId, checkIn, checkOut)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.create(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Room is not available");
    }

    @Test
    void getById_shouldReturnBookingResponse() {
        Long userId = 10L;
        Long bookingId = 100L;

        Booking booking = new Booking();
        booking.setId(bookingId);

        BookingResponse expectedResponse = new BookingResponse(
                bookingId, "Hotel", null, 1L, null, null,
                LocalDate.now(), LocalDate.now().plusDays(2), null,
                BookingStatus.HOLD, null, LocalDateTime.now(), null
        );

        when(bookingRepository.findByIdAndUserId(bookingId, userId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingResponse(booking)).thenReturn(expectedResponse);

        BookingResponse result = bookingService.getById(userId, bookingId);

        assertThat(result.bookingId()).isEqualTo(bookingId);
    }

    @Test
    void getById_shouldThrowWhenBookingNotFound() {
        when(bookingRepository.findByIdAndUserId(999L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getById(10L, 999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    void cancelMyBooking_shouldSetStatusCancelled() {
        Long userId = 10L;
        Long bookingId = 100L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatus.HOLD);

        when(bookingRepository.findByIdAndUserId(bookingId, userId)).thenReturn(Optional.of(booking));

        bookingService.cancelMyBooking(userId, bookingId);

        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void cancelMyBooking_shouldThrowWhenBookingNotFound() {
        when(bookingRepository.findByIdAndUserId(999L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelMyBooking(10L, 999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");
    }
}
