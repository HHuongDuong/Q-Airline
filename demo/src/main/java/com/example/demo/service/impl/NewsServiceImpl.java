package com.example.demo.service.impl;

import com.example.demo.dto.NewsRequestDTO;
import com.example.demo.dto.NewsResponseDTO;
import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.NewsService;
import com.example.demo.service.UserService;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
    private static final String NEWS_NOT_FOUND = "News not found";
    private final NewsRepository newsRepository;
    private final UserService userService;

    @Override
    @Transactional
    public NewsResponseDTO createNews(NewsRequestDTO newsRequestDTO) {
        News news = new News();
        news.setTitle(newsRequestDTO.getTitle());
        news.setContent(newsRequestDTO.getContent());
        news.setImageUrl(newsRequestDTO.getImageUrl());
        // Default values, can be made configurable or part of DTO if needed
        news.setType("NEWS"); 
        news.setPublishDate(LocalDateTime.now());
        news.setExpiryDate(LocalDateTime.now().plusYears(1)); // Example: expires in 1 year
        news.setActive(true);
        // Assign current logged-in user as author if applicable, otherwise handle null
        User currentUser = userService.getUserById(userService.getCurrentUser().getId());
        news.setCreatedBy(currentUser);
        
        News savedNews = newsRepository.save(news);
        return convertToDto(savedNews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponseDTO> getAllActiveNews() {
        return newsRepository.findByActiveTrueOrderByPublishDateDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsResponseDTO getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NEWS_NOT_FOUND));
        return convertToDto(news);
    }

    @Override
    @Transactional
    public NewsResponseDTO updateNews(Long id, NewsRequestDTO newsRequestDTO) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NEWS_NOT_FOUND));
        
        news.setTitle(newsRequestDTO.getTitle());
        news.setContent(newsRequestDTO.getContent());
        news.setImageUrl(newsRequestDTO.getImageUrl());
        // Update other fields if necessary
        // news.setType(newsRequestDTO.getType());
        // news.setPublishDate(newsRequestDTO.getPublishDate());
        // news.setExpiryDate(newsRequestDTO.getExpiryDate());
        // news.setActive(newsRequestDTO.isActive());
        
        News updatedNews = newsRepository.save(news);
        return convertToDto(updatedNews);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new ResourceNotFoundException(NEWS_NOT_FOUND);
        }
        newsRepository.deleteById(id);
    }

    // Keep these methods if still needed for other functionalities that use raw News entity
    @Override
    @Transactional(readOnly = true)
    public List<News> getAllNews() {
        return newsRepository.findAll().stream().toList();
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
        return newsRepository.findByCreatedBy_Id(userId);
    }

    private NewsResponseDTO convertToDto(News news) {
        NewsResponseDTO dto = new NewsResponseDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setImageUrl(news.getImageUrl());
        dto.setCreatedAt(news.getCreatedAt());
        dto.setUpdatedAt(news.getUpdatedAt());
        return dto;
    }
} 