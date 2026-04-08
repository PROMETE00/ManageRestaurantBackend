package com.restaurante.api.catalog.dto;

import java.math.BigDecimal;

public record PublicMenuItem(
        Integer id,
        String name,
        BigDecimal price,
        String category,
        String type,
        String imageUrl,
        BigDecimal rating,
        Integer availableQuantity
) {
}
