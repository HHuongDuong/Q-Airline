package com.example.demo.controller;

import com.example.demo.dto.NewsDTO;
import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.service.NewsService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO, 
                                            @AuthenticationPrincipal User currentUser) {
        NewsDTO createdNews = newsService.createNews(newsDTO, currentUser);
        return ResponseEntity.ok(createdNews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, 
                                            @RequestBody NewsDTO newsDTO) {
        NewsDTO updatedNews = newsService.updateNews(id, newsDTO);
        return ResponseEntity.ok(updatedNews);
    }

    @GetMapping
    public ResponseEntity<List<NewsDTO>> getAllActiveNews() {
        List<NewsDTO> news = newsService.getAllActiveNews();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        NewsDTO news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<News>> searchNews(@RequestParam String keyword) {
        List<News> news = newsService.searchNews(keyword);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<News>> getNewsByType(@PathVariable String type) {
        List<News> news = newsService.getNewsByType(type);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<News>> getActivePromotions() {
        List<News> promotions = newsService.getActivePromotions();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<News>> getNewsByUser(@PathVariable Long userId) {
        List<News> news = newsService.getNewsByUser(userId);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<News>> getNewsByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<News> news = newsService.getNewsByDateRange(start, end);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<News>> getRecentNews(@RequestParam LocalDateTime date) {
        List<News> news = newsService.getRecentNews(date);
        return ResponseEntity.ok(news);
    }
} 