package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.dto.BookingCreatedEvent;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        UUID roomId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        LocalDateTime now = LocalDateTime.now();

        BookingCreateRequest request = new BookingCreateRequest(
                userId, roomId, checkIn, checkOut, 3
        );

        Hotel hotel = new Hotel();
        hotel.setName("Hyatt Regency Tashkent");

        Room room = new Room();
        room.setId(roomId);
        room.setHotel(hotel);

        Booking mappedEntity = new Booking();
        Booking savedBooking = new Booking();
        savedBooking.setId(bookingId);
        savedBooking.setCreatedAt(now);

        BookingResponse expectedResponse = new BookingResponse(
                bookingId, userId,"Hyatt Regency Tashkent","Navoi Ave",roomId,"H-101","DELUXE",checkIn,checkOut,null,BookingStatus.HOLD,null,3,now
        );

        // Настройка моков
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(bookingRepository.isRoomAvailable(eq(roomId), any(), any())).thenReturn(true);
        when(bookingMapper.toEntity(request)).thenReturn(mappedEntity);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toBookingResponse(savedBooking)).thenReturn(expectedResponse);

        // WHEN
        BookingResponse result = bookingService.create(request);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.bookingId()).isEqualTo(bookingId);

        // Проверка отправки в Kafka
        ArgumentCaptor<BookingCreatedEvent> eventCaptor = ArgumentCaptor.forClass(BookingCreatedEvent.class);
        verify(kafkaProducerService).sendBookingCreated(eventCaptor.capture());

        BookingCreatedEvent sentEvent = eventCaptor.getValue();
        assertThat(sentEvent.bookingId()).isEqualTo(bookingId);
        assertThat(sentEvent.userId()).isEqualTo(userId);
        assertThat(sentEvent.guestsCount()).isEqualTo(expectedResponse.guestsCount());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void create_shouldThrowWhenRoomNotFound() {
        UUID roomId = UUID.randomUUID();
        BookingCreateRequest request = new BookingCreateRequest(
                UUID.randomUUID(), roomId, LocalDate.now(), LocalDate.now().plusDays(1), 1
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Room not found");

        verifyNoInteractions(bookingRepository);
        verifyNoInteractions(kafkaProducerService);
    }

    @Test
    void create_shouldThrowWhenRoomNotAvailable() {
        UUID roomId = UUID.randomUUID();
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);
        BookingCreateRequest request = new BookingCreateRequest(
                UUID.randomUUID(), roomId, checkIn, checkOut, 2
        );

        Room room = new Room();
        room.setId(roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(bookingRepository.isRoomAvailable(roomId, checkIn, checkOut)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.create(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Room is not available");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnBookingResponse() {
        UUID userId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();

        BookingResponse expectedResponse = new BookingResponse(
                bookingId, userId, "Hotel", null, roomId, null, null,
                LocalDate.now(), LocalDate.now().plusDays(2), null,
                BookingStatus.HOLD, null, 2, LocalDateTime.now()
        );

        when(bookingRepository.findByIdAndUserId(bookingId, userId)).thenReturn(Optional.of(new Booking()));
        when(bookingMapper.toBookingResponse(any())).thenReturn(expectedResponse);

        BookingResponse result = bookingService.getById(userId, bookingId);

        assertThat(result.userId()).isEqualTo(userId);
    }
    @Test
    void cancelMyBooking_shouldSetStatusCancelled() {
        UUID userId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.HOLD);

        when(bookingRepository.findByIdAndUserId(bookingId, userId)).thenReturn(Optional.of(booking));

        bookingService.cancelMyBooking(userId, bookingId);

        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
    }
}