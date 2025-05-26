package com.movingpack.movingpack.delivery;

import com.movingpack.movingpack.postalcode.PostalCodeService;
import com.movingpack.movingpack.driver.Driver;
import com.movingpack.movingpack.driver.DriverRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final PostalCodeService postalCodeService;

    public DeliveryService(DeliveryRepository deliveryRepository, DriverRepository driverRepository, PostalCodeService postalCodeService) {
        this.deliveryRepository = deliveryRepository;
        this.driverRepository = driverRepository;
        this.postalCodeService = postalCodeService;
    }

    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    public Delivery save(DeliveryDto delivery) {
        Driver driver = driverRepository.findById(delivery.driverId())
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        postalCodeService.consult(delivery.postalCode());

        Delivery toSave = new Delivery();
        toSave.setPostalCode(delivery.postalCode());
        toSave.setDeliveryTime(delivery.deliveryTime());
        toSave.setDriver(driver);

        return deliveryRepository.save(toSave);
    }

    public Delivery update(Long id, Delivery newData) {
        return deliveryRepository.findById(id)
                .map(existing -> {
                    existing.setPostalCode(newData.getPostalCode());
                    existing.setDeliveryTime(newData.getDeliveryTime());
                    existing.setDriver(newData.getDriver());
                    return deliveryRepository.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));
    }

    public void delete(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new EntityNotFoundException("Delivery not found");
        }
        deliveryRepository.deleteById(id);
    }
}