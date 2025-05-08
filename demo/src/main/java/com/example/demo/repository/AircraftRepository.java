package com.example.demo.repository;

import com.example.demo.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByCode(String code);
    boolean existsByCode(String code);
    List<Aircraft> findByManufacturer(String manufacturer);
    List<Aircraft> findByModel(String model);
} 