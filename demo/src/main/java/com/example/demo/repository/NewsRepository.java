package com.example.demo.repository;

import com.example.demo.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByPublishedAtBetween(LocalDateTime start, LocalDateTime end);
    List<News> findByTitleContaining(String keyword);
    List<News> findByContentContaining(String keyword);
    List<News> findByPublishedAtBefore(LocalDateTime date);
} 