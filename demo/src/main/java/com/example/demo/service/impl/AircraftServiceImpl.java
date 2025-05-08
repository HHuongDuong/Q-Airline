package com.example.demo.service.impl;

import com.example.demo.entity.Aircraft;
import com.example.demo.repository.AircraftRepository;
import com.example.demo.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AircraftServiceImpl implements AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    @Override
    public Aircraft createAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }

    @Override
    public Aircraft updateAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }

    @Override
    public void deleteAircraft(Long id) {
        aircraftRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aircraft> getAircraftById(Long id) {
        return aircraftRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aircraft> getAircraftByCode(String code) {
        return aircraftRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aircraft> getAircraftByManufacturer(String manufacturer) {
        return aircraftRepository.findByManufacturer(manufacturer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aircraft> getAircraftByModel(String model) {
        return aircraftRepository.findByModel(model);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return aircraftRepository.existsByCode(code);
    }
} 