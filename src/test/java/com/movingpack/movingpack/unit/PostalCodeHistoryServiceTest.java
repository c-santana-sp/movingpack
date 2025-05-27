package com.movingpack.movingpack.unit;

import com.movingpack.movingpack.externalapi.ExternalApiClient;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepDto;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepErrorDto;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepResult;
import com.movingpack.movingpack.externalapi.dto.ExternalApiResponse;
import com.movingpack.movingpack.postalcode.PostalCodeService;
import com.movingpack.movingpack.postalcode.history.PostalCodeHistory;
import com.movingpack.movingpack.postalcode.history.PostalCodeHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PostalCodeHistoryServiceTest {

    private PostalCodeHistoryService postalCodeHistoryService;
    private ExternalApiClient externalApiClient;
    private PostalCodeService postalCodeService;

    @BeforeEach
    void setup() {
        postalCodeHistoryService = mock(PostalCodeHistoryService.class);
        externalApiClient = mock(ExternalApiClient.class);
        postalCodeService = new PostalCodeService(postalCodeHistoryService, externalApiClient);
    }

    @Test
    void consult_whenSuccess_shouldSaveHistoryAndReturnResponse() {
        var data = new ExternalApiCepDto("12345-678", "Rua A", "Bairro B", "Cidade C", "Estado D");
        var success = new ExternalApiCepResult.Success(data);
        var response = new ExternalApiResponse(200, success);

        when(externalApiClient.fetchCep("12345-678")).thenReturn(response);

        ExternalApiResponse result = postalCodeService.consult("12345-678");

        assertEquals(response, result);
        verify(postalCodeHistoryService, times(1)).save(any(PostalCodeHistory.class));

        ArgumentCaptor<PostalCodeHistory> captor = ArgumentCaptor.forClass(PostalCodeHistory.class);
        verify(postalCodeHistoryService).save(captor.capture());

        PostalCodeHistory saved = captor.getValue();
        assertEquals("12345-678", saved.getPostalCode());
        assertEquals(200, saved.getStatusCode());
        assertEquals(success, saved.getPayload());
    }

    @Test
    void consult_whenFailure_shouldSaveHistoryAndThrowException() {
        var data = new ExternalApiCepErrorDto("Todos os serviços de CEP retornaram erro.", "service_error", "name", null);
        var failure = new ExternalApiCepResult.Failure(data);
        var response = new ExternalApiResponse(404, failure);

        when(externalApiClient.fetchCep("11111-111")).thenReturn(response);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> postalCodeService.consult("11111-111")
        );

        assertEquals("Postal Code not found", exception.getMessage());
        verify(postalCodeHistoryService, times(1)).save(any(PostalCodeHistory.class));

        ArgumentCaptor<PostalCodeHistory> captor = ArgumentCaptor.forClass(PostalCodeHistory.class);
        verify(postalCodeHistoryService).save(captor.capture());

        PostalCodeHistory saved = captor.getValue();
        assertEquals("11111-111", saved.getPostalCode());
        assertEquals(404, saved.getStatusCode());
        assertEquals(failure, saved.getPayload());
    }
}
