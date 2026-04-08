package com.restaurante.api.reservations.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record PublicReservationResponse(
        String reference,
        String customerName,
        String email,
        String phone,
        LocalDate date,
        LocalTime time,
        Integer partySize,
        Integer tableNumber,
        String location,
        String status
) {
}
