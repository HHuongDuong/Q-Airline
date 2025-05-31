package com.example.demo.service.impl;

import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.NewsService;
import com.example.demo.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final UserService userService;

    @Override
    @Transactional
    public News createNews(News news, User user) {
        news.setAuthor(user);
        news.setCreatedAt(LocalDateTime.now());
        news.setActive(true);
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public News updateNews(Long id, News newsDetails) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        news.setTitle(newsDetails.getTitle());
        news.setContent(newsDetails.getContent());
        news.setType(newsDetails.getType());
        news.setActive(newsDetails.isActive());
        news.setUpdatedAt(LocalDateTime.now());
        
        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        newsRepository.delete(news);
    }

    @Override
    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end) {
        return newsRepository.findByCreatedAtBetween(start, end);
    }

    @Override
    public List<News> searchNewsByTitle(String keyword) {
        return newsRepository.findByTitleContainingIgnoreCase(keyword);
    }

    @Override
    public List<News> searchNewsByContent(String keyword) {
        return newsRepository.findByContentContainingIgnoreCase(keyword);
    }

    @Override
    public List<News> getRecentNews(LocalDateTime date) {
        return newsRepository.findByCreatedAtAfterOrderByCreatedAtDesc(date);
    }

    @Override
    public List<News> getNewsByType(String type) {
        return newsRepository.findByType(type);
    }

    @Override
    public List<News> searchNews(String keyword) {
        return newsRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<News> getActiveNews() {
        return newsRepository.findByActiveTrue();
    }

    @Override
    public List<News> getActivePromotions() {
        return newsRepository.findByTypeAndActiveTrue("PROMOTION");
    }

    @Override
    public List<News> getNewsByUser(Long userId) {
        return newsRepository.findByAuthorId(userId);
    }
} 