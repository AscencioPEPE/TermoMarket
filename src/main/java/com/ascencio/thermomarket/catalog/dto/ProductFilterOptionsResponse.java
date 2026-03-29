package com.ascencio.thermomarket.catalog.dto;

import java.util.List;

public record ProductFilterOptionsResponse(
        List<String> brands,
        List<String> categories,
        List<String> colors,
        List<Integer> capacitiesOz
) {
}
