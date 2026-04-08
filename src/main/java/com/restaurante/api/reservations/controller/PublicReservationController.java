package com.restaurante.api.reservations.controller;

import com.restaurante.api.reservations.dto.PublicReservationRequest;
import com.restaurante.api.reservations.dto.PublicReservationResponse;
import com.restaurante.api.reservations.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/reservations")
@RequiredArgsConstructor
public class PublicReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicReservationResponse createReservation(@Valid @RequestBody PublicReservationRequest request) {
        return reservationService.createPublicReservation(request);
    }

    @GetMapping("/{reference}")
    public PublicReservationResponse getReservation(@PathVariable String reference) {
        return reservationService.getPublicReservation(reference);
    }
}
