package com.movingpack.movingpack.postalcode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepDto;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepServiceErrorDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostalCodeResponseDto(
    boolean success,
    ExternalApiCepDto data,
    List<ExternalApiCepServiceErrorDto> errors,
    String message,
    int status
) {}
