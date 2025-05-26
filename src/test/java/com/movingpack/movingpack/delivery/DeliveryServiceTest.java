package com.movingpack.movingpack.delivery;

import com.movingpack.movingpack.driver.Driver;
import com.movingpack.movingpack.driver.DriverDto;
import com.movingpack.movingpack.driver.DriverRepository;
import com.movingpack.movingpack.postalcode.PostalCodeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private PostalCodeService postalCodeService;

    @InjectMocks
    private DeliveryService deliveryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnDelivery() {
        LocalDateTime now = LocalDateTime.now();
        DriverDto driverDto = new DriverDto(1L, "John Driver");
        DeliveryDto deliveryToCreate = new DeliveryDto(null, "12345", now, driverDto.id());

        Driver driverToReturn = new Driver(1L, "John Driver", null);
        Delivery deliveryToReturn = new Delivery(1L, "12345", now, driverToReturn);

        when(driverRepository.findById(any())).thenReturn(Optional.of(driverToReturn));
        when(deliveryRepository.save(any())).thenReturn(deliveryToReturn);

        Delivery created = deliveryService.save(deliveryToCreate);

        assertThat(created).isEqualTo(deliveryToReturn);
    }

    @Test
    void findById_whenFound_shouldReturnDelivery() {
        Delivery deliveryToReturn = new Delivery(1L, "1234", LocalDateTime.now(),
                new Driver(1L, "John Driver", null));
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(deliveryToReturn));

        Delivery found = deliveryService.findById(1L).get();

        assertThat(found).isEqualTo(deliveryToReturn);
    }

    @Test
    void update_whenFound_shouldUpdateAndSave() {
        Driver driver = new Driver(1L, "John Driver", null);
        Delivery delivery = new Delivery(1L, "12345", LocalDateTime.now(), driver);
        Delivery updateData = new Delivery(null, "54321", LocalDateTime.now().plusDays(1), driver);
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any())).thenReturn(delivery);

        Delivery updated = deliveryService.update(1L, updateData);

        assertThat(updated.getPostalCode()).isEqualTo("54321");
        verify(deliveryRepository).save(delivery);
    }

    @Test
    void update_whenNotFound_shouldThrow() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deliveryService.update(1L, new Delivery()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Delivery not found");
    }

    @Test
    void delete_whenNotFoundShouldThrow() {
        when(deliveryRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> deliveryService.delete(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Delivery not found");
    }

    @Test
    void delete_whenFoundShouldDelete() {
        when(deliveryRepository.existsById(1L)).thenReturn(true);

        deliveryService.delete(1L);

        verify(deliveryRepository).deleteById(1L);
    }
}
