package com.ascencio.thermomarket.order.dto;

import com.ascencio.thermomarket.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        Long shippingAddressId,
        OrderStatus status,
        BigDecimal total,
        String notes,
        OffsetDateTime orderDate,
        List<OrderItemResponse> items,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
