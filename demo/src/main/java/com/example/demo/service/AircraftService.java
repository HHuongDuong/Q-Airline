package com.example.demo.service;

import com.example.demo.entity.Aircraft;
import java.util.List;
import java.util.Optional;

public interface AircraftService {
    Aircraft createAircraft(Aircraft aircraft);
    Aircraft updateAircraft(Aircraft aircraft);
    void deleteAircraft(Long id);
    Optional<Aircraft> getAircraftById(Long id);
    Optional<Aircraft> getAircraftByCode(String code);
    List<Aircraft> getAllAircraft();
    List<Aircraft> getAircraftByManufacturer(String manufacturer);
    List<Aircraft> getAircraftByModel(String model);
    boolean existsByCode(String code);
} 