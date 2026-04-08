package com.restaurante.api.users.dto;

import com.restaurante.api.model.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
        @NotNull Role role
) {
}
