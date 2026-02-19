package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Маппинг из Entity в Response (для клиента)
    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "hotelName", source = "room.hotel.name")
    @Mapping(target = "hotelAddress", source = "room.hotel.address")
    @Mapping(target = "roomType", source = "room.roomType")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "checkIn", source = "checkInDate")
    @Mapping(target = "checkOut", source = "checkOutDate")
    BookingResponse toBookingResponse(Booking booking);

    // Маппинг из Request в Entity (при создании)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "holdUntil", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "checkInDate", source = "checkInDate")
    @Mapping(target = "checkOutDate", source = "checkOutDate")
    Booking toEntity(BookingCreateRequest request);
}