package com.example.demo.service;

import com.example.demo.entity.News;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsService {
    News createNews(News news);
    News updateNews(News news);
    void deleteNews(Long id);
    Optional<News> getNewsById(Long id);
    List<News> getAllNews();
    List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end);
    List<News> searchNewsByTitle(String keyword);
    List<News> searchNewsByContent(String keyword);
    List<News> getRecentNews(LocalDateTime date);
} 