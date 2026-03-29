package com.ascencio.thermomarket.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank @Size(max = 140) String street,
        @NotBlank @Size(max = 20) String exteriorNumber,
        @Size(max = 20) String interiorNumber,
        @NotBlank @Size(max = 120) String neighborhood,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 100) String state,
        @NotBlank @Pattern(regexp = "\\d{5}", message = "must contain 5 digits") String postalCode,
        @Size(max = 250) String references
) {
}
