package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "hotelName", source = "room.hotel.name")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "totalPrice", source = "totalPrice") // Цена, рассчитанная в сервисе
    @Mapping(target = "paymentType", source = "paymentType")
    BookingResponse toBookingResponse(Booking booking);
}
