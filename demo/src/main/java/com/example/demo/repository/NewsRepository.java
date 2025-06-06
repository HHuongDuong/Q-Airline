package com.example.demo.repository;

import com.example.demo.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByActiveTrue();
    List<News> findByActiveTrueOrderByPublishDateDesc();
    List<News> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<News> findByTitleContainingIgnoreCase(String keyword);
    List<News> findByContentContainingIgnoreCase(String keyword);
    List<News> findByType(String type);
    List<News> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);
    List<News> findByTypeAndActiveTrue(String type);
    List<News> findByCreatedBy_Id(Long userId);
    List<News> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);
    List<News> findByPublishDateAfter(LocalDateTime date);
    List<News> findByPublishDateBetween(LocalDateTime start, LocalDateTime end);
    List<News> findByTypeAndActiveTrueAndPublishDateBeforeAndExpiryDateAfter(
        String type, LocalDateTime now, LocalDateTime now2);
    
    List<News> findByActiveTrueAndPublishDateBeforeAndExpiryDateAfter(
        LocalDateTime now, LocalDateTime now2);
} 