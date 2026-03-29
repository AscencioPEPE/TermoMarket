package com.ascencio.thermomarket.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrderRequest(
        @NotNull Long customerId,
        @NotNull Long shippingAddressId,
        @Size(max = 300) String notes,
        @NotEmpty List<@Valid OrderItemRequest> items
) {
}
