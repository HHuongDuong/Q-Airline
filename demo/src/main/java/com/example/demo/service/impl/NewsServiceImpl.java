package com.example.demo.service.impl;

import com.example.demo.dto.NewsDTO;
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

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private static final String NEWS_NOT_FOUND = "News not found";
    private final NewsRepository newsRepository;
    private final UserService userService;

    @Override
    @Transactional
    public NewsDTO createNews(NewsDTO newsDTO, User author) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setType(newsDTO.getType());
        news.setPublishDate(LocalDateTime.now());
        news.setExpiryDate(newsDTO.getExpiryDate());
        news.setActive(true);
        news.setCreatedBy(author);
        
        News savedNews = newsRepository.save(news);
        return convertToDTO(savedNews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getAllActiveNews() {
        return newsRepository.findByActiveTrueOrderByPublishDateDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NEWS_NOT_FOUND));
        return convertToDTO(news);
    }

    @Override
    @Transactional
    public NewsDTO updateNews(Long id, NewsDTO newsDTO) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NEWS_NOT_FOUND));
        
        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setType(newsDTO.getType());
        news.setExpiryDate(newsDTO.getExpiryDate());
        news.setActive(newsDTO.isActive());
        
        News updatedNews = newsRepository.save(news);
        return convertToDTO(updatedNews);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new ResourceNotFoundException(NEWS_NOT_FOUND);
        }
        newsRepository.deleteById(id);
    }

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

    private NewsDTO convertToDTO(News news) {
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setType(news.getType());
        dto.setPublishDate(news.getPublishDate());
        dto.setExpiryDate(news.getExpiryDate());
        dto.setActive(news.isActive());
        if (news.getCreatedBy() != null) {
            dto.setAuthorId(news.getCreatedBy().getId());
            dto.setAuthorName(news.getCreatedBy().getFullName());
        }
        return dto;
    }
} 