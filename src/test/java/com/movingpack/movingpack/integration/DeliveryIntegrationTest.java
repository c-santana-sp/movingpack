package com.movingpack.movingpack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movingpack.movingpack.config.TestcontainersConfiguration;
import com.movingpack.movingpack.delivery.DeliveryDto;
import com.movingpack.movingpack.driver.DriverRepository;
import com.movingpack.movingpack.driver.Driver;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfiguration.Initializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeliveryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DriverRepository driverRepository;

    private static Long driverId;
    private static Long deliveryId;

    @BeforeAll
    static void setupDriver(@Autowired DriverRepository driverRepository) {
        Driver driver = new Driver();
        driver.setName("Test Driver");
        driver = driverRepository.save(driver);
        driverId = driver.getId();
    }

    @Test
    @Order(1)
    void testCreateDeliveryFoundPostalCode() throws Exception {
        var postalCode = "03258060";
        var deliveryDto = new DeliveryDto(
            null,
            postalCode,
            LocalDateTime.of(2025, 5, 26, 4, 4, 16),
            driverId
        );

        var result = mockMvc.perform(post("/api/v1/deliveries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deliveryDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.postalCode").value(postalCode))
            .andExpect(jsonPath("$.driverId").value(driverId))
            .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), DeliveryDto.class);
        deliveryId = response.id();
        Assertions.assertNotNull(deliveryId);
    }

    @Test
    @Order(2)
    void testCreateDeliveryNotFoundPostalCode() throws Exception {
        var postalCode = "11111111";
        var deliveryDto = new DeliveryDto(
                null,
                postalCode,
                LocalDateTime.of(2025, 5, 26, 4, 4, 16),
                driverId
        );

        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Postal Code not found"))
                .andReturn();
    }

    @Test
    void testsCreateDeliveryWhenPostalCodeInvalid() throws Exception {
        String postalCode = "000asdf";

        var deliveryDto = new DeliveryDto(
                null,
                postalCode,
                LocalDateTime.of(2025, 5, 26, 4, 4, 16),
                driverId
        );

        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.postalCode").value("Invalid CEP format. Expected 12345678 or 12345-678"))
                .andReturn();
    }

    @Test
    @Order(3)
    void testGetDeliveryById() throws Exception {
        mockMvc.perform(get("/api/v1/deliveries/{id}", deliveryId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(deliveryId))
            .andExpect(jsonPath("$.postalCode").value("03258060"))
            .andExpect(jsonPath("$.driverId").value(driverId));
    }

    @Test
    @Order(4)
    void testUpdateDelivery() throws Exception {
        var updatedDeliveryDto = new DeliveryDto(
            null,
            "54321",
            LocalDateTime.of(2025, 5, 27, 5, 5, 5),
            driverId
        );

        mockMvc.perform(put("/api/v1/deliveries/{id}", deliveryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDeliveryDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(deliveryId))
            .andExpect(jsonPath("$.postalCode").value("54321"))
            .andExpect(jsonPath("$.deliveryTime").value("2025-05-27 05:05:05"))
            .andExpect(jsonPath("$.driverId").value(driverId));
    }

    @Test
    @Order(5)
    void testGetAllDeliveries() throws Exception {
        mockMvc.perform(get("/api/v1/deliveries"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(deliveryId));
    }

    @Test
    @Order(6)
    void testDeleteDelivery() throws Exception {
        mockMvc.perform(delete("/api/v1/deliveries/{id}", deliveryId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/deliveries/{id}", deliveryId))
            .andExpect(status().isNotFound());
    }
}
