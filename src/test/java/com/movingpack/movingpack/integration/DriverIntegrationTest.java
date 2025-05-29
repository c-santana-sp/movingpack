package com.movingpack.movingpack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movingpack.movingpack.config.TestcontainersConfiguration;
import com.movingpack.movingpack.driver.DriverDto;
import com.movingpack.movingpack.driver.DriverRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfiguration.Initializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DriverIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DriverRepository driverRepository;

    private static Long driverId;

    @BeforeAll
    static void setupDriver(@Autowired DriverRepository driverRepository) {
        driverRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testCreateDriver() throws Exception {
        var dto = new DriverDto(null, "John Doe");

        var result = mockMvc.perform(post("/api/v1/drivers")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), DriverDto.class);
        driverId = response.id();
        Assertions.assertNotNull(driverId);
    }

    @Test
    @Order(2)
    void testGetDriverById() throws Exception {
        mockMvc.perform(get("/api/v1/drivers/{id}", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(driverId))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @Order(3)
    void testUpdateDriver() throws Exception {
        var updatedDto = new DriverDto(null, "Jane Smith");

        mockMvc.perform(put("/api/v1/drivers/{id}", driverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(driverId))
                .andExpect(jsonPath("$.name").value("Jane Smith"));
    }

    @Test
    @Order(4)
    void testGetAllDrivers() throws Exception {
        mockMvc.perform(get("/api/v1/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(driverId));
    }

    @Test
    @Order(5)
    void testDeleteDriver() throws Exception {
        mockMvc.perform(delete("/api/v1/drivers/{id}", driverId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/drivers/{id}", driverId))
                .andExpect(status().isNotFound());
    }
}
