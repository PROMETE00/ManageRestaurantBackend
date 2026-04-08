package com.restaurante.api.auth.dto;

import com.restaurante.api.model.Role;

public record AuthUser(
        Integer id,
        String name,
        String email,
        Role role
) {
}
