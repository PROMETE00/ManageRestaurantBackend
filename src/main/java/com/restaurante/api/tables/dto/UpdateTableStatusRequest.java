package com.restaurante.api.tables.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTableStatusRequest(
        @NotBlank String state
) {
}
