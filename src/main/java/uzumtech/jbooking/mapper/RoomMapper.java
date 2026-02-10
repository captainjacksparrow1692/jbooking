package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uzumtech.jbooking.dto.request.RoomCreateRequest;
import uzumtech.jbooking.dto.response.RoomResponse;
import uzumtech.jbooking.entity.Room;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "roomAvailabilityStatus", ignore = true)
    Room toEntity(RoomCreateRequest request);

    @Mapping(target = "roomAvailabilityStatus", source = "rooomAvailabilityStatus")
    RoomResponse toResponse(Room room);
}
