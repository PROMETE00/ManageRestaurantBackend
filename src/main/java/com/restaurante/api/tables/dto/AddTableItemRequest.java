package com.restaurante.api.tables.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddTableItemRequest(
        @NotNull Integer productId,
        @NotNull @Min(1) Integer quantity
) {
}
