package com.movingpack.movingpack.postalcode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.movingpack.movingpack.config.TestcontainersConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.movingpack.movingpack.config.TestcontainersConfiguration.wireMockServer;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfiguration.Initializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostalCodeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnSuccessResponseWhenValidPostalCode() throws Exception {
        String postalCode = "03255000";

        mockMvc.perform(get("/api/v1/postal-code/consult/{code}", postalCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cep").value(postalCode))
                .andExpect(jsonPath("$.data.street").value("Rua José Antônio Fontes"))
                .andExpect(jsonPath("$.data.neighborhood").value("Vila Tolstoi"));
    }

    @Test
    void shouldReturnFailureResponseWhenPostalCodeNotFound() throws Exception {
        String postalCode = "00000000";

        mockMvc.perform(get("/api/v1/postal-code/consult/{code}", postalCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Postal Code not found"))
                .andReturn();
    }

    @Test
    void shouldReturnErrorResponseWhenPostalCodeInvalid() throws Exception {
        String postalCode = "000asdf";

        mockMvc.perform(get("/api/v1/postal-code/consult/{code}", postalCode))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("consult.code: Invalid CEP format. Expected 12345678 or 12345-678"))
                .andReturn();
    }
}
