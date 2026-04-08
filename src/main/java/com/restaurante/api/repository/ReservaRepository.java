package com.restaurante.api.repository;

import com.restaurante.api.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    boolean existsByMesaIdAndFechaAndHora(Integer mesaId, LocalDate fecha, LocalTime hora);

    List<Reserva> findAllByOrderByFechaAscHoraAsc();

    Optional<Reserva> findByPublicId(String publicId);
}
