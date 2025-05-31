package com.example.demo.service;

import com.example.demo.entity.News;
import com.example.demo.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {
    News createNews(News news, User user);
    News updateNews(Long id, News news);
    void deleteNews(Long id);
    News getNewsById(Long id);
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