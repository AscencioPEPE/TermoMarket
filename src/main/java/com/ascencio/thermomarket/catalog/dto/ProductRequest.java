package com.ascencio.thermomarket.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 140) String slug,
        @NotBlank @Size(max = 500) String description,
        @NotBlank @Size(max = 80) String brand,
        @NotBlank @Size(max = 80) String category,
        @NotBlank @Size(max = 60) String color,
        @NotBlank @Size(max = 80) String material,
        @NotNull @Min(1) Integer capacityOz,
        @Size(max = 180) String imageAlt,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @NotNull @Min(0) Integer stock,
        @NotNull Boolean active
) {
}
