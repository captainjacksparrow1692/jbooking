package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    BookingResponse toBookingResponse(Booking booking);
}
