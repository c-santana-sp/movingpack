package com.movingpack.movingpack.externalapi.dto;

public record ExternalApiResponse(
    Integer statusCode,
    ExternalApiCepResult result
) {}
