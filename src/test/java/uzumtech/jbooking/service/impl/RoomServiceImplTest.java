package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.constant.enums.RoomType;
import uzumtech.jbooking.dto.request.RoomSearchRequest;
import uzumtech.jbooking.dto.response.RoomResponse;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.exception.BookingValidationException;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.RoomMapper;
import uzumtech.jbooking.repository.RoomRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomMapper roomMapper;

    @InjectMocks
    RoomServiceImpl roomService;

    @Test
    void searchRooms_shouldReturnPageOfRooms() {
        UUID hotelId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();

        LocalDateTime checkIn = LocalDateTime.now().plusDays(1);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(3);

        RoomSearchRequest request = new RoomSearchRequest(
                hotelId, checkIn, checkOut, null, null, 2
        );
        Pageable pageable = PageRequest.of(0, 10);

        Room room = Room.builder()
                .id(roomId) // Теперь UUID
                .roomNumber("101")
                .price(BigDecimal.valueOf(100))
                .capacity(3)
                .roomType(RoomType.STANDARD)
                .build();

        RoomResponse response = new RoomResponse(
                roomId, "101", RoomType.STANDARD, BigDecimal.valueOf(100),
                3, null, null
        );

        Page<Room> roomPage = new PageImpl<>(List.of(room), pageable, 1);

        when(roomRepository.searchRooms(
                eq(hotelId), eq(checkIn.toLocalDate()), eq(checkOut.toLocalDate()),
                eq(2), eq(null), eq(null), any(Pageable.class)
        )).thenReturn(roomPage);
        when(roomMapper.toResponse(room)).thenReturn(response);

        Page<RoomResponse> result = roomService.searchRooms(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().roomNumber()).isEqualTo("101");
        assertThat(result.getContent().getFirst().id()).isEqualTo(roomId);
    }

    @Test
    void searchRooms_shouldThrowWhenCheckInNotBeforeCheckOut() {
        UUID hotelId = UUID.randomUUID();
        LocalDateTime checkIn = LocalDateTime.now().plusDays(3);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(1);

        RoomSearchRequest request = new RoomSearchRequest(
                hotelId, checkIn, checkOut, null, null, 2
        );

        assertThatThrownBy(() -> roomService.searchRooms(request, PageRequest.of(0, 10)))
                .isInstanceOf(BookingValidationException.class);
    }

    @Test
    void searchRooms_shouldThrowWhenSameDates() {
        UUID hotelId = UUID.randomUUID();
        LocalDateTime sameDate = LocalDateTime.now().plusDays(1);

        RoomSearchRequest request = new RoomSearchRequest(
                hotelId, sameDate, sameDate, null, null, 2
        );

        assertThatThrownBy(() -> roomService.searchRooms(request, PageRequest.of(0, 10)))
                .isInstanceOf(BookingValidationException.class);
    }

    @Test
    void getById_shouldReturnRoomResponse() {
        UUID roomId = UUID.randomUUID();
        Room room = Room.builder()
                .id(roomId)
                .roomNumber("101")
                .price(BigDecimal.valueOf(100))
                .capacity(2)
                .roomType(RoomType.STANDARD)
                .build();
        RoomResponse expected = new RoomResponse(
                roomId, "101", RoomType.STANDARD, BigDecimal.valueOf(100),
                2, null, null
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomMapper.toResponse(room)).thenReturn(expected);

        RoomResponse result = roomService.getById(roomId);

        assertThat(result.id()).isEqualTo(roomId);
        assertThat(result.roomNumber()).isEqualTo("101");
    }

    @Test
    void getById_shouldThrowWhenRoomNotFound() {
        UUID randomId = UUID.randomUUID();
        when(roomRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.getById(randomId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Room not found");
    }
}