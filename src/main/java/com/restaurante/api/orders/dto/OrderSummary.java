package com.restaurante.api.orders.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummary(
        Integer id,
        String status,
        BigDecimal total,
        String clientName,
        Integer tableNumber,
        LocalDateTime placedAt
) {
}
