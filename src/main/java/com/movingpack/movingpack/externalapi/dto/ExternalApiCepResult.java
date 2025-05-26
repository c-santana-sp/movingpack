package com.movingpack.movingpack.externalapi.dto;

public sealed interface ExternalApiCepResult permits ExternalApiCepResult.Success, ExternalApiCepResult.Failure {
    record Success(ExternalApiCepDto data) implements ExternalApiCepResult {}
    record Failure(ExternalApiCepErrorDto error) implements ExternalApiCepResult {}
}
