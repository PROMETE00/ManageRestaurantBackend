package com.restaurante.api.users.controller;

import com.restaurante.api.users.dto.CreateUserRequest;
import com.restaurante.api.users.dto.UpdateUserRoleRequest;
import com.restaurante.api.users.dto.UserSummary;
import com.restaurante.api.users.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserManagementService userManagementService;

    @GetMapping
    public List<UserSummary> listUsers() {
        return userManagementService.listUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserSummary createUser(@Valid @RequestBody CreateUserRequest request) {
        return userManagementService.createUser(request);
    }

    @PatchMapping("/{userId}/role")
    public UserSummary updateRole(
            @PathVariable Integer userId,
            @Valid @RequestBody UpdateUserRoleRequest request
    ) {
        return userManagementService.updateRole(userId, request);
    }
}
