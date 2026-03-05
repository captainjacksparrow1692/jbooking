package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    //Entity в Response
    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "hotelName", source = "room.hotel.name")
    @Mapping(target = "hotelAddress", source = "room.hotel.address")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "roomType", source = "room.roomType")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "checkIn", source = "checkInDate")
    @Mapping(target = "checkOut", source = "checkOutDate")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "paymentType", ignore = true)
    BookingResponse toBookingResponse(Booking booking);

    //Request в Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "holdUntil", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "guestsCount", source = "guestsCount")
    Booking toEntity(BookingCreateRequest request);
}