package com.example.demo.repository;

import com.example.demo.entity.DashboardStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardStatsRepository extends JpaRepository<DashboardStats, Long> {
    List<DashboardStats> findByDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT d FROM DashboardStats d WHERE d.date = (SELECT MAX(d2.date) FROM DashboardStats d2)")
    DashboardStats findLatestStats();
    
    @Query("SELECT d FROM DashboardStats d WHERE d.date >= ?1 ORDER BY d.date DESC")
    List<DashboardStats> findStatsFromDate(LocalDateTime date);
} 