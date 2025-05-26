package com.movingpack.movingpack.delivery;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;

    public DeliveryController(DeliveryService deliveryService, DeliveryMapper deliveryMapper) {
        this.deliveryService = deliveryService;
        this.deliveryMapper = deliveryMapper;
    }

    @GetMapping
    public List<DeliveryDto> findAll() {
        return deliveryService.findAll( ).stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DeliveryDto findById(@PathVariable Long id) {
        return deliveryMapper.toDto(deliveryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryDto create(@Valid @RequestBody DeliveryDto dto) {
        Delivery saved = deliveryService.save(dto);
        return deliveryMapper.toDto(saved);
    }

    @PutMapping("/{id}")
    public DeliveryDto update(@PathVariable Long id, @RequestBody DeliveryDto dto) {
        Delivery updated = deliveryService.update(id, deliveryMapper.toEntity(dto));
        return deliveryMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
