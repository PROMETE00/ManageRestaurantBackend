package com.restaurante.api.reservations.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationSummary(
        String reference,
        String customerName,
        LocalDate date,
        LocalTime time,
        Integer partySize,
        Integer tableNumber,
        String location,
        String waiterName
) {
}
