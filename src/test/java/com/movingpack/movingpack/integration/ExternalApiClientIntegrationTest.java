package com.movingpack.movingpack.integration;

import com.movingpack.movingpack.config.TestcontainersConfiguration;
import com.movingpack.movingpack.externalapi.ExternalApiClient;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepResult;
import com.movingpack.movingpack.externalapi.dto.ExternalApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = TestcontainersConfiguration.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExternalApiClientIntegrationTest {

    @Autowired
    private ExternalApiClient externalApiClient;

    @Test
    void shouldReturnMockedSuccessResponse() {
        String cep = "03258060";
        ExternalApiResponse response = externalApiClient.fetchCep(cep);

        assertEquals(200, response.statusCode());
        assertInstanceOf(ExternalApiCepResult.Success.class, response.result());
    }

    @Test
    void shouldReturnMockedNotFoundFailureResponse() {
        String cep = "11111111";
        ExternalApiResponse response = externalApiClient.fetchCep(cep);

        assertEquals(404, response.statusCode());
        assertInstanceOf(ExternalApiCepResult.Failure.class, response.result());
    }

    @Test
    void shouldReturnMockedUnknownFailureResponse() {
        String cep = "00000000";
        ExternalApiResponse response = externalApiClient.fetchCep(cep);

        assertEquals(500, response.statusCode());
    }
}
