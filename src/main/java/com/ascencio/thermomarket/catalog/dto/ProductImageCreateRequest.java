package com.ascencio.thermomarket.catalog.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductImageCreateRequest(
        @NotBlank @Size(max = 255) String storageKey,
        @NotBlank @Size(max = 255) String originalFilename,
        @NotBlank @Size(max = 100) String contentType,
        @NotNull @Positive @Max(10485760) Long sizeBytes,
        @Min(1) Integer width,
        @Min(1) Integer height,
        @NotNull Boolean primaryImage,
        @NotNull @Min(0) Integer sortOrder
) {
}
