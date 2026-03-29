package com.ascencio.thermomarket.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductImageUpdateRequest(
        @NotNull Boolean primaryImage,
        @NotNull @Min(0) Integer sortOrder
) {
}
