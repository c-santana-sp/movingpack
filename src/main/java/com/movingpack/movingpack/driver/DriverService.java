package com.movingpack.movingpack.driver;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }

    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver update(Long id, Driver newData) {
        return driverRepository.findById(id)
                .map(existing -> {
                    existing.setName(newData.getName());
                    return driverRepository.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));
    }

    public void delete(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new EntityNotFoundException("Driver not found");
        }
        driverRepository.deleteById(id);
    }
}
