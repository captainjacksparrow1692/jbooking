package uzumtech.jbooking.dto;

import uzumtech.jbooking.constant.enums.GuestType;

public record GuestDto(
        String firstName,
        String lastName,
        GuestType guestType,
        Integer age
){
}
