package com.movingpack.movingpack.postalcode;

import com.movingpack.movingpack.externalapi.dto.ExternalApiCepResult;
import com.movingpack.movingpack.externalapi.dto.ExternalApiResponse;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/postal-code")
public class PostalCodeController {

    private final PostalCodeService service;

    public PostalCodeController(PostalCodeService service) {
        this.service = service;
    }

    @GetMapping("/consult/{code}")
    public ResponseEntity<PostalCodeResponseDto> consult(
            @PathVariable @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Invalid CEP format. Expected 12345678 or 12345-678") String code) {
        ExternalApiResponse response = service.consult(code);

        PostalCodeResponseDto dto = switch (response.result()) {
            case ExternalApiCepResult.Success s ->
                    new PostalCodeResponseDto(true, s.data(), null, null, response.statusCode());
            case ExternalApiCepResult.Failure f ->
                    new PostalCodeResponseDto(false, null, f.error().errors(), f.error().message(), response.statusCode());
        };

        return ResponseEntity.status(dto.status()).body(dto);
    }
}
