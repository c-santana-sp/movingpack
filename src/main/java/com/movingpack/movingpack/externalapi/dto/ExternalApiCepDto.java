package com.movingpack.movingpack.externalapi.dto;

public record ExternalApiCepDto(
        String cep,
        String state,
        String neighborhood,
        String street,
        String service
) {}
