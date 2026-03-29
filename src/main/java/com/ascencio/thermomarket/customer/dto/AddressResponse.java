package com.ascencio.thermomarket.customer.dto;

import java.time.OffsetDateTime;

public record AddressResponse(
        Long id,
        Long customerId,
        String street,
        String exteriorNumber,
        String interiorNumber,
        String neighborhood,
        String city,
        String state,
        String postalCode,
        String references,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
