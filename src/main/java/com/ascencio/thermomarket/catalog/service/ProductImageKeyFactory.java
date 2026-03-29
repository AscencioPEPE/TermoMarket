package com.ascencio.thermomarket.catalog.service;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ProductImageKeyFactory {

    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
    );

    public String createProductOriginalKey(Long productId, String contentType) {
        String extension = EXTENSIONS.get(contentType);
        if (extension == null) {
            throw new IllegalArgumentException("Unsupported image content type: %s".formatted(contentType));
        }

        return "products/%d/original/%s.%s".formatted(productId, UUID.randomUUID(), extension);
    }
}
