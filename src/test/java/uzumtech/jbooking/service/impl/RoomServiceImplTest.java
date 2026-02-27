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
import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;
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
        LocalDateTime checkIn = LocalDateTime.now().plusDays(1);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(3);

        RoomSearchRequest request = new RoomSearchRequest(
                1L, checkIn, checkOut, null, null, 2
        );
        Pageable pageable = PageRequest.of(0, 10);

        Room room = Room.builder()
                .id(1L).roomNumber("101").price(BigDecimal.valueOf(100))
                .capacity(3).roomType(RoomType.STANDARD)
                .build();
        RoomResponse response = new RoomResponse(
                1L, "101", RoomType.STANDARD, BigDecimal.valueOf(100),
                3, null, null
        );

        Page<Room> roomPage = new PageImpl<>(List.of(room), pageable, 1);

        when(roomRepository.searchAvailableRooms(
                eq(1L), eq(checkIn.toLocalDate()), eq(checkOut.toLocalDate()),
                eq(2), eq(null), eq(null), any(Pageable.class)
        )).thenReturn(roomPage);
        when(roomMapper.toResponse(room)).thenReturn(response);

        Page<RoomResponse> result = roomService.searchRooms(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().roomNumber()).isEqualTo("101");
    }

    @Test
    void searchRooms_shouldThrowWhenCheckInNotBeforeCheckOut() {
        LocalDateTime checkIn = LocalDateTime.now().plusDays(3);
        LocalDateTime checkOut = LocalDateTime.now().plusDays(1);

        RoomSearchRequest request = new RoomSearchRequest(
                1L, checkIn, checkOut, null, null, 2
        );

        assertThatThrownBy(() -> roomService.searchRooms(request, PageRequest.of(0, 10)))
                .isInstanceOf(BookingValidationException.class);
    }

    @Test
    void searchRooms_shouldThrowWhenSameDates() {
        LocalDateTime sameDate = LocalDateTime.now().plusDays(1);

        RoomSearchRequest request = new RoomSearchRequest(
                1L, sameDate, sameDate, null, null, 2
        );

        assertThatThrownBy(() -> roomService.searchRooms(request, PageRequest.of(0, 10)))
                .isInstanceOf(BookingValidationException.class);
    }

    @Test
    void getById_shouldReturnRoomResponse() {
        Room room = Room.builder()
                .id(1L).roomNumber("101").price(BigDecimal.valueOf(100))
                .capacity(2).roomType(RoomType.STANDARD)
                .build();
        RoomResponse expected = new RoomResponse(
                1L, "101", RoomType.STANDARD, BigDecimal.valueOf(100),
                2, null, null
        );

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomMapper.toResponse(room)).thenReturn(expected);

        RoomResponse result = roomService.getById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.roomNumber()).isEqualTo("101");
    }

    @Test
    void getById_shouldThrowWhenRoomNotFound() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.getById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Room not found");
    }
}
