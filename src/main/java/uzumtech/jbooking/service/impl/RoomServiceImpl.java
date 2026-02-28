package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.constant.enums.Error;
import uzumtech.jbooking.dto.request.RoomSearchRequest;
import uzumtech.jbooking.dto.response.RoomResponse;
import uzumtech.jbooking.exception.BookingValidationException;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.RoomMapper;
import uzumtech.jbooking.repository.RoomRepository;
import uzumtech.jbooking.service.RoomService;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true) // Только чтение для пользователя
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;
    RoomMapper roomMapper;

    @Override
    public Page<RoomResponse> searchRooms(RoomSearchRequest request, Pageable pageable) {
        log.info("Searching rooms with criteria: {}", request);

        if (request.checkIn() != null && request.checkOut() != null
                && !request.checkIn().isBefore(request.checkOut())) {
            throw new BookingValidationException(
                    Error.INVALID_BOOKING_DATES_ERROR_CODE.getCode(),
                    Error.INVALID_BOOKING_DATES_ERROR_CODE.getMessage()
            );
        }

        Pageable safePageable = pageable;
        if (safePageable == null || safePageable.isUnpaged()) {
            safePageable = PageRequest.of(0, Constant.DEFAULT_PAGE_SIZE);
        } else if (safePageable.getPageSize() > Constant.MAX_PAGE_SIZE) {
            safePageable = PageRequest.of(
                    safePageable.getPageNumber(),
                    Constant.MAX_PAGE_SIZE,
                    safePageable.getSort()
            );
        }

        return roomRepository.searchAvailableRooms(
                        request.hotelId(),
                        request.checkIn(),
                        request.checkOut(),
                        request.guestsCount(),
                        request.boardBasis(),
                        request.cancellationPolicyType(),
                        safePageable
                )
                .map(roomMapper::toResponse);
    }

    @Override
    public RoomResponse getById(UUID id) {
        return roomRepository.findById(id)
                .map(roomMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }
}