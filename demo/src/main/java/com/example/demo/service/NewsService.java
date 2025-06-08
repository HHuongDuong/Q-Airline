package com.example.demo.service;

import com.example.demo.dto.NewsRequestDTO;
import com.example.demo.dto.NewsResponseDTO;
import com.example.demo.entity.News;
import com.example.demo.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {
    NewsResponseDTO createNews(NewsRequestDTO newsRequestDTO);
    List<NewsResponseDTO> getAllActiveNews();
    NewsResponseDTO getNewsById(Long id);
    NewsResponseDTO updateNews(Long id, NewsRequestDTO newsRequestDTO);
    void deleteNews(Long id);
    List<News> getAllNews();
    List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end);
    List<News> searchNewsByTitle(String keyword);
    List<News> searchNewsByContent(String keyword);
    List<News> getRecentNews(LocalDateTime date);
    List<News> getNewsByType(String type);
    List<News> searchNews(String keyword);
    List<News> getActiveNews();
    List<News> getActivePromotions();
    List<News> getNewsByUser(Long userId);
} 