package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.*;
import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.constant.enums.HotelBrand;

import java.util.List;

public record HotelCreateRequest(
        @NotBlank(message = "Hotel name is required")
        String name,

        @NotNull(message = "City ID is required")
        Long cityId, // Привязка к конкретному городу из твоего CityRepository

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Accommodation type is required")
        AccommodationType type, // HOTEL, VILLA, APARTMENT

        @NotNull(message = "Hotel brand is required")
        HotelBrand brand, // CHAIN, INDEPENDENT

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5")
        Integer stars, // Официальное количество звезд

        @Size(max = 2000)
        String description,

        List<String> amenities // Список удобств (WiFi, Pool, Parking, etc)
) {
}
