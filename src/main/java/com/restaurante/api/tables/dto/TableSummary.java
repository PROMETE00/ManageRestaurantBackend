package com.restaurante.api.tables.dto;

import java.math.BigDecimal;

public record TableSummary(
        Integer id,
        Integer number,
        Integer capacity,
        String location,
        String state,
        String waiterName,
        Integer itemCount,
        BigDecimal total
) {
}
