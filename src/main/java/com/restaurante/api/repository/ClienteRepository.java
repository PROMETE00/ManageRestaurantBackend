package com.restaurante.api.repository;

import com.restaurante.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findFirstByEmailIgnoreCase(String email);

    Optional<Cliente> findFirstByTelefono(String telefono);
}
