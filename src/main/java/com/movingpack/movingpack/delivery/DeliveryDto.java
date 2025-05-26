package com.movingpack.movingpack.delivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record DeliveryDto(
        Long id,

        @NotBlank(message = "CEP is required")
        @Pattern(
                regexp = "^\\d{5}-?\\d{3}$",
                message = "Invalid CEP format. Expected 12345678 or 12345-678"
        )
        String postalCode,

        @NotNull(message = "deliveryTime is required in format yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime deliveryTime,

        @NotNull(message = "driverId is required")
        Long driverId
) {}
