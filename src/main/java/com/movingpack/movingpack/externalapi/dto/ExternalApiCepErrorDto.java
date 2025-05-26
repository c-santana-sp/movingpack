package com.movingpack.movingpack.externalapi.dto;

import java.util.List;

public record ExternalApiCepErrorDto(
        String message,
        String type,
        String name,
        List<ExternalApiCepServiceErrorDto> errors
) {}