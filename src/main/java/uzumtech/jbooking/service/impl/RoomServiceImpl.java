package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.dto.request.RoomSearchRequest;
import uzumtech.jbooking.dto.response.RoomResponse;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.RoomMapper;
import uzumtech.jbooking.repository.HotelRepository;
import uzumtech.jbooking.repository.RoomRepository;
import uzumtech.jbooking.service.RoomService;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true) // Только чтение для пользователя
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;
    RoomMapper roomMapper;
    HotelRepository hotelRepository;

    @Override
    public Page<RoomResponse> getRoomsByHotel(Long hotelId, Pageable pageable) {
        log.info("Fetching rooms for hotel id: {}", hotelId);

        // Проверяем существование отеля перед поиском комнат
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found");
        }

        return roomRepository.findByHotelId(hotelId, pageable)
                .map(roomMapper::toResponse);
    }

    @Override
    public Page<RoomResponse> searchRooms(RoomSearchRequest request, Pageable pageable) {
        log.info("Searching rooms with criteria: {}", request);

        // Здесь в идеале должна быть логика через Specification или @Query
        // для проверки дат бронирования. Пока возвращаем по hotelId.
        return roomRepository.findByHotelId(request.hotelId(), pageable)
                .map(roomMapper::toResponse);
    }

    @Override
    public RoomResponse getById(Long id) {
        return roomRepository.findById(id)
                .map(roomMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }
}