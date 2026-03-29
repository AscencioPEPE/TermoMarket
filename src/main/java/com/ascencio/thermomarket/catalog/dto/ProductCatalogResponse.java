package com.ascencio.thermomarket.catalog.dto;

import java.util.List;

public record ProductCatalogResponse(
        List<ProductResponse> items,
        String nextCursor,
        boolean hasNext,
        int limit
) {
}
