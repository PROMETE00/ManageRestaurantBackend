package com.restaurante.api.repository;

import com.restaurante.api.model.MenuMesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuMesaRepository extends JpaRepository<MenuMesa, Integer> {

    List<MenuMesa> findByMesaId(Integer mesaId);

    Optional<MenuMesa> findFirstByMesaIdAndProductoId(Integer mesaId, Integer productoId);
}
