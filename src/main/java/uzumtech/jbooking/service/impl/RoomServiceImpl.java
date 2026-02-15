package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.enums.RoomAvailabilityStatus;
import uzumtech.jbooking.dto.request.RoomCreateRequest;
import uzumtech.jbooking.dto.response.RoomResponse;
import uzumtech.jbooking.entity.Hotel;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.RoomMapper;
import uzumtech.jbooking.repository.HotelRepository;
import uzumtech.jbooking.repository.RoomRepository;
import uzumtech.jbooking.service.RoomService;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;
    RoomMapper roomMapper;
    HotelRepository hotelRepository;

    @Override
    @Transactional
    public RoomResponse create(RoomCreateRequest request){
        Hotel hotel = hotelRepository.findById(request.hotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        Room room = roomMapper.toEntity(request);
        room.setHotel(hotel);
        room.setRoomAvailabilityStatus(RoomAvailabilityStatus.AVAILABLE);

        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override
    public Page<RoomResponse> getRoomsByHotel(Long hotelId, Pageable  pageable) {
        return roomRepository.findByHotelIdAndCapacityGreaterThanEqualAndRoomAvailabilityStatus(
                hotelId, 1, RoomAvailabilityStatus.AVAILABLE, pageable
        )
                .map(roomMapper::toResponse);
    }

    @Override
    public RoomResponse getById(Long id){
        return roomRepository.findById(id)
                .map(roomMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @Override
    @Transactional
    public void updateAvailability(Long roomId, RoomAvailabilityStatus status){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        room.setRoomAvailabilityStatus(status);
        roomRepository.save(room);
    }
}
