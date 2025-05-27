package com.movingpack.movingpack.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    public DriverController(DriverService driverService, DriverMapper driverMapper) {
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @GetMapping
    public List<DriverDto> findAll() {
        return driverService.findAll().stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DriverDto findById(@PathVariable Long id) {
        return driverMapper.toDto(driverService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDto create(@RequestBody DriverDto dto) {
        Driver saved = driverService.save(driverMapper.toEntity(dto));
        return driverMapper.toDto(saved);
    }

    @PutMapping("/{id}")
    public DriverDto update(@PathVariable Long id, @RequestBody DriverDto dto) {
        Driver updated = driverService.update(id, driverMapper.toEntity(dto));
        return driverMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}