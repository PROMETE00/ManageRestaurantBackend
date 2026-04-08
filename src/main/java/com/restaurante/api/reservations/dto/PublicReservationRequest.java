package com.restaurante.api.reservations.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record PublicReservationRequest(
        @NotBlank String customerName,
        @Email String email,
        @NotBlank String phone,
        @NotNull @FutureOrPresent LocalDate date,
        @NotNull LocalTime time,
        @NotNull @Min(1) Integer partySize,
        Integer tableId
) {
}
