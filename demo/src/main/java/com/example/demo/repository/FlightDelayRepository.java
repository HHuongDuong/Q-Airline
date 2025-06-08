package com.example.demo.repository;

import com.example.demo.entity.FlightDelay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDelayRepository extends JpaRepository<FlightDelay, Long> {
}