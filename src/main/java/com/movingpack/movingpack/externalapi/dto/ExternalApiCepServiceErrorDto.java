package com.movingpack.movingpack.externalapi.dto;

public record ExternalApiCepServiceErrorDto(
    String name,
    String message,
    String service
) {}
