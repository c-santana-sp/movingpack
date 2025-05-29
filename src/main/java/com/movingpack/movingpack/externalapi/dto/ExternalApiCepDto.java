package com.movingpack.movingpack.externalapi.dto;

public record ExternalApiCepDto(
        String cep,
        String state,
        String city,
        String neighborhood,
        String street,
        String service
) {}
