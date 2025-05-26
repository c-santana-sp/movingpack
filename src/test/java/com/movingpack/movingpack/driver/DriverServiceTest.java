package com.movingpack.movingpack.driver;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnDriver() {
        Driver toCreate = new Driver(null, "John Doe", null);
        Driver toReturn = new Driver(1L, "John Doe", null);
        when(driverRepository.save(ArgumentMatchers.any())).thenReturn(toReturn);

        Driver created = driverService.save(toCreate);

        assertThat(created).isEqualTo(toReturn);
        verify(driverRepository).save(toCreate);
    }

    @Test
    void findById_whenFound_shouldReturnDriver() {
        Driver driver = new Driver(1L, "John Doe", null);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver found = driverService.findById(1L).get();

        assertThat(found).isEqualTo(driver);
    }

    @Test
    void update_whenFound_shouldUpdateAndSave() {
        Driver driver = new Driver(1L, "John Doe", null);
        Driver updatedData = new Driver(null, "Jane Smith", null);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any())).thenReturn(driver);

        Driver updated = driverService.update(1L, updatedData);

        assertThat(updated.getName()).isEqualTo("Jane Smith");
        verify(driverRepository).save(driver);
    }

    @Test
    void update_whenNotFound_shouldThrow() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.update(1L, new Driver()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Driver not found");
    }

    @Test
    void delete_whenNotFoundShouldThrow() {
        when(driverRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> driverService.delete(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Driver not found");
    }

    @Test
    void delete_whenFoundShouldDelete() {
        when(driverRepository.existsById(1L)).thenReturn(true);

        driverService.delete(1L);

        verify(driverRepository).deleteById(1L);
    }
}
