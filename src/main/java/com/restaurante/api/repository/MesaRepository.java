package com.restaurante.api.repository;

import com.restaurante.api.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    List<Mesa> findAllByOrderByNumeroAsc();
}
