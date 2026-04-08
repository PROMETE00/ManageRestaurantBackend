package com.restaurante.api.reservations.controller;

import com.restaurante.api.reservations.dto.ReservationSummary;
import com.restaurante.api.reservations.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff/reservations")
@RequiredArgsConstructor
public class StaffReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationSummary> listReservations() {
        return reservationService.listStaffReservations();
    }
}
