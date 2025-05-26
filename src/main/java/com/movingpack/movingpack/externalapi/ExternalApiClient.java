package com.movingpack.movingpack.externalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepDto;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepErrorDto;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepResult;
import com.movingpack.movingpack.externalapi.dto.ExternalApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ExternalApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    public ExternalApiClient(RestTemplate restTemplate,
                             @Value("${external.api.base-url}") String baseUrl,
                             ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
    }

    public ExternalApiResponse fetchCep(String cep) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/mock/api/cep/v1/{cep}")
                .buildAndExpand(cep)
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String body = response.getBody();
        HttpStatusCode status = response.getStatusCode();

        try {
            if (status.is2xxSuccessful()) {
                ExternalApiCepDto data = objectMapper.readValue(body, ExternalApiCepDto.class);
                return new ExternalApiResponse(status.value(), new ExternalApiCepResult.Success(data));
            } else {
                 if (status.is5xxServerError()) {
                     ExternalApiCepErrorDto unknowError = new ExternalApiCepErrorDto(
                             "Postal Code External API Returned: " + body,
                             null,
                             "Unknown Error",
                             null
                     );
                     return new ExternalApiResponse(status.value(), new ExternalApiCepResult.Failure(unknowError));
                 }
                ExternalApiCepErrorDto data = objectMapper.readValue(body, ExternalApiCepErrorDto.class);
                return new ExternalApiResponse(status.value(), new ExternalApiCepResult.Failure(data));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse response body: " + body, ex);
        }
    }
}
