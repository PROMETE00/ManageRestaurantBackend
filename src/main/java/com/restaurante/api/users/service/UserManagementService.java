package com.restaurante.api.users.service;

import com.restaurante.api.common.exception.ConflictException;
import com.restaurante.api.common.exception.ResourceNotFoundException;
import com.restaurante.api.model.Role;
import com.restaurante.api.model.Usuario;
import com.restaurante.api.repository.UsuarioRepository;
import com.restaurante.api.users.dto.CreateUserRequest;
import com.restaurante.api.users.dto.UpdateUserRoleRequest;
import com.restaurante.api.users.dto.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserSummary> listUsers() {
        return usuarioRepository.findAll().stream()
                .sorted(Comparator.comparing(Usuario::getCreatedAt).reversed())
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public UserSummary createUser(CreateUserRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ConflictException("A user with that email already exists");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.name().trim());
        usuario.setEmail(request.email().trim().toLowerCase());
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setRole(request.role());

        return toSummary(usuarioRepository.save(usuario));
    }

    @Transactional
    public UserSummary updateRole(Integer userId, UpdateUserRoleRequest request) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        usuario.setRole(request.role());
        return toSummary(usuarioRepository.save(usuario));
    }

    @Transactional
    public void bootstrapAdminIfNeeded(String name, String email, String password) {
        if (usuarioRepository.count() > 0) {
            return;
        }
        if (isBlank(name) || isBlank(email) || isBlank(password)) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setNombre(name.trim());
        admin.setEmail(email.trim().toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        usuarioRepository.save(admin);
    }

    private UserSummary toSummary(Usuario usuario) {
        return new UserSummary(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getCreatedAt()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
