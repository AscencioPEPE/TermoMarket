package com.ascencio.thermomarket.catalog.dto;

import java.time.OffsetDateTime;

public record ProductImagePresignResponse(
        String storageKey,
        String uploadUrl,
        String publicUrl,
        OffsetDateTime expiresAt,
        String contentType
) {
}
