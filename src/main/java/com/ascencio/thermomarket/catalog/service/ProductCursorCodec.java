package com.ascencio.thermomarket.catalog.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class ProductCursorCodec {

    private static final String PREFIX = "product:";

    public String encode(Long id) {
        String raw = PREFIX + id;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public Long decode(String cursor) {
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
            if (!decoded.startsWith(PREFIX)) {
                throw new IllegalArgumentException("Invalid cursor");
            }

            return Long.parseLong(decoded.substring(PREFIX.length()));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid cursor");
        }
    }
}
