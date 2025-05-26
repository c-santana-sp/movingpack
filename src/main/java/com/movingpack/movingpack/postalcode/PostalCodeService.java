package com.movingpack.movingpack.postalcode;

import com.movingpack.movingpack.externalapi.ExternalApiClient;
import com.movingpack.movingpack.externalapi.dto.ExternalApiCepResult;
import com.movingpack.movingpack.externalapi.dto.ExternalApiResponse;
import com.movingpack.movingpack.postalcode.history.PostalCodeHistory;
import com.movingpack.movingpack.postalcode.history.PostalCodeHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PostalCodeService {

    private final PostalCodeHistoryService postalCodeHistoryService;
    private final ExternalApiClient externalApiClient;

    public PostalCodeService(PostalCodeHistoryService postalCodeHistoryService, ExternalApiClient externalApiClient) {
        this.postalCodeHistoryService = postalCodeHistoryService;
        this.externalApiClient = externalApiClient;
    }

    public ExternalApiResponse consult(String code) {
        ExternalApiResponse cepResponse = externalApiClient.fetchCep(code);

        switch (cepResponse.result()) {
            case ExternalApiCepResult.Success s -> handleSuccess(s, cepResponse.statusCode());
            case ExternalApiCepResult.Failure f -> handleFailure(f, code, cepResponse.statusCode());
        }

        return cepResponse;
    }

    private void handleSuccess(ExternalApiCepResult.Success cepResponse, Integer statusCode) {
        PostalCodeHistory postalCodeHistory = new PostalCodeHistory();
        postalCodeHistory.setPostalCode(cepResponse.data().cep());
        postalCodeHistory.setStatusCode(statusCode);
        postalCodeHistory.setPayload(cepResponse);

        postalCodeHistoryService.save(postalCodeHistory);
    }

    private void handleFailure(ExternalApiCepResult.Failure cepResponse, String code, Integer statusCode) {
        PostalCodeHistory postalCodeHistory = new PostalCodeHistory();
        postalCodeHistory.setPostalCode(code);
        postalCodeHistory.setStatusCode(statusCode);
        postalCodeHistory.setPayload(cepResponse);

        postalCodeHistoryService.save(postalCodeHistory);

        throw new EntityNotFoundException("Postal Code not found");
    }
}
