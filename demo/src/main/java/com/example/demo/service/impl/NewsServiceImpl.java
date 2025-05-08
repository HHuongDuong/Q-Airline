package com.example.demo.service.impl;

import com.example.demo.entity.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public News createNews(News news) {
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @Override
    public News updateNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end) {
        return newsRepository.findByPublishedAtBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> searchNewsByTitle(String keyword) {
        return newsRepository.findByTitleContaining(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> searchNewsByContent(String keyword) {
        return newsRepository.findByContentContaining(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> getRecentNews(LocalDateTime date) {
        return newsRepository.findByPublishedAtBefore(date);
    }
} 