package com.ascencio.thermomarket.catalog.dto;

import java.time.OffsetDateTime;

public record ProductImageResponse(
        Long id,
        String storageKey,
        String originalFilename,
        String contentType,
        Long sizeBytes,
        Integer width,
        Integer height,
        Boolean primaryImage,
        Integer sortOrder,
        String publicUrl,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
