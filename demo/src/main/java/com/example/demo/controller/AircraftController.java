package com.example.demo.controller;

import com.example.demo.entity.Aircraft;
import com.example.demo.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @PostMapping
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
        return ResponseEntity.ok(aircraftService.createAircraft(aircraft));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft aircraft) {
        aircraft.setId(id);
        return ResponseEntity.ok(aircraftService.updateAircraft(aircraft));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        return aircraftService.getAircraftById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Aircraft> getAircraftByCode(@PathVariable String code) {
        return aircraftService.getAircraftByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<List<Aircraft>> getAircraftByManufacturer(@PathVariable String manufacturer) {
        return ResponseEntity.ok(aircraftService.getAircraftByManufacturer(manufacturer));
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<Aircraft>> getAircraftByModel(@PathVariable String model) {
        return ResponseEntity.ok(aircraftService.getAircraftByModel(model));
    }

    @GetMapping("/check/code/{code}")
    public ResponseEntity<Boolean> checkCodeExists(@PathVariable String code) {
        return ResponseEntity.ok(aircraftService.existsByCode(code));
    }
} 