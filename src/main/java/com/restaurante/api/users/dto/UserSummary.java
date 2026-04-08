package com.restaurante.api.users.dto;

import com.restaurante.api.model.Role;

import java.time.LocalDateTime;

public record UserSummary(
        Integer id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
