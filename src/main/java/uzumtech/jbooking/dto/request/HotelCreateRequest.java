package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.constant.enums.HotelBrand;

public record HotelCreateRequest(

        @NotBlank
        Long cityId,

        @NotBlank(message = "Name of hotel ")
        String name,

        String description,

        @NotNull
        AccommodationType accommodationType,

        @NotNull
        HotelBrand hotelBrand
) {
}
