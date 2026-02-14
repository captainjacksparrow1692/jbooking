package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingHistoryResponse;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.BookingHistory;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "hotelName", source = "room.hotel.name")
    @Mapping(target = "totalPrice", ignore = true)
    BookingResponse toBookingResponse(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    Booking toEntity(BookingCreateRequest request);

    BookingHistoryResponse toHistoryResponse(BookingHistory history);
}