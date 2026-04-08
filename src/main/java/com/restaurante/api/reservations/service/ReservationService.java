package com.restaurante.api.reservations.service;

import com.restaurante.api.common.exception.ConflictException;
import com.restaurante.api.common.exception.ResourceNotFoundException;
import com.restaurante.api.model.Cliente;
import com.restaurante.api.model.EstadoMesaEnum;
import com.restaurante.api.model.Mesa;
import com.restaurante.api.model.Reserva;
import com.restaurante.api.repository.ClienteRepository;
import com.restaurante.api.repository.MesaRepository;
import com.restaurante.api.repository.ReservaRepository;
import com.restaurante.api.reservations.dto.PublicReservationRequest;
import com.restaurante.api.reservations.dto.PublicReservationResponse;
import com.restaurante.api.reservations.dto.ReservationSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public PublicReservationResponse createPublicReservation(PublicReservationRequest request) {
        Mesa mesa = resolveTable(request);

        if (mesa.getCapacidad() != null && request.partySize() > mesa.getCapacidad()) {
            throw new ConflictException("The selected table does not have enough capacity");
        }

        if (reservaRepository.existsByMesaIdAndFechaAndHora(mesa.getId(), request.date(), request.time())) {
            throw new ConflictException("That table already has a reservation for the selected date and time");
        }

        Cliente cliente = resolveClient(request);

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setMesa(mesa);
        reserva.setFecha(request.date());
        reserva.setHora(request.time());
        reserva.setCantidad(request.partySize());
        reserva.setMesero(mesa.getMesero());

        Reserva saved = reservaRepository.save(reserva);

        if (request.date().equals(LocalDate.now()) && mesa.getEstado() == EstadoMesaEnum.libre) {
            mesa.setEstado(EstadoMesaEnum.reservada);
            mesaRepository.save(mesa);
        }

        return toPublicResponse(saved);
    }

    @Transactional(readOnly = true)
    public PublicReservationResponse getPublicReservation(String publicId) {
        Reserva reserva = reservaRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        return toPublicResponse(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservationSummary> listStaffReservations() {
        return reservaRepository.findAllByOrderByFechaAscHoraAsc().stream()
                .map(reserva -> new ReservationSummary(
                        reserva.getPublicId(),
                        reserva.getCliente() != null ? reserva.getCliente().getNombre() : "Walk-in",
                        reserva.getFecha(),
                        reserva.getHora(),
                        reserva.getCantidad(),
                        reserva.getMesa() != null ? reserva.getMesa().getNumero() : null,
                        reserva.getMesa() != null ? reserva.getMesa().getUbicacion() : null,
                        reserva.getMesero() != null ? reserva.getMesero().getNombre() : null
                ))
                .toList();
    }

    private Cliente resolveClient(PublicReservationRequest request) {
        Cliente cliente = null;
        if (request.email() != null && !request.email().isBlank()) {
            cliente = clienteRepository.findFirstByEmailIgnoreCase(request.email().trim()).orElse(null);
        }
        if (cliente == null && request.phone() != null && !request.phone().isBlank()) {
            cliente = clienteRepository.findFirstByTelefono(request.phone().trim()).orElse(null);
        }
        if (cliente == null) {
            cliente = new Cliente();
        }

        cliente.setNombre(request.customerName().trim());
        cliente.setTelefono(request.phone().trim());
        if (request.email() != null && !request.email().isBlank()) {
            cliente.setEmail(request.email().trim().toLowerCase());
        }

        return clienteRepository.save(cliente);
    }

    private Mesa resolveTable(PublicReservationRequest request) {
        if (request.tableId() != null) {
            return mesaRepository.findById(request.tableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        }

        return mesaRepository.findAllByOrderByNumeroAsc().stream()
                .filter(mesa -> mesa.getCapacidad() == null || mesa.getCapacidad() >= request.partySize())
                .filter(mesa -> !reservaRepository.existsByMesaIdAndFechaAndHora(mesa.getId(), request.date(), request.time()))
                .findFirst()
                .orElseThrow(() -> new ConflictException("No available table matches that party size for the selected time"));
    }

    private PublicReservationResponse toPublicResponse(Reserva reserva) {
        return new PublicReservationResponse(
                reserva.getPublicId(),
                reserva.getCliente() != null ? reserva.getCliente().getNombre() : null,
                reserva.getCliente() != null ? reserva.getCliente().getEmail() : null,
                reserva.getCliente() != null ? reserva.getCliente().getTelefono() : null,
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getCantidad(),
                reserva.getMesa() != null ? reserva.getMesa().getNumero() : null,
                reserva.getMesa() != null ? reserva.getMesa().getUbicacion() : null,
                reserva.getMesa() != null ? reserva.getMesa().getEstado().name() : "reservada"
        );
    }
}
