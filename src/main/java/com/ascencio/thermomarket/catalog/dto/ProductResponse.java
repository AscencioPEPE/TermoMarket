package com.ascencio.thermomarket.catalog.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponse(
        Long id,
        String name,
        String slug,
        String description,
        String brand,
        String category,
        String color,
        String material,
        Integer capacityOz,
        String imageAlt,
        BigDecimal price,
        Integer stock,
        Boolean active,
        String primaryImageUrl,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
