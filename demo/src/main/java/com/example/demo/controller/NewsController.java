package com.example.demo.controller;

import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.service.NewsService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(newsService.createNews(news, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News newsDetails) {
        return ResponseEntity.ok(newsService.updateNews(id, newsDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<News>> getActiveNews() {
        return ResponseEntity.ok(newsService.getActiveNews());
    }

    @GetMapping("/promotions")
    public ResponseEntity<List<News>> getActivePromotions() {
        return ResponseEntity.ok(newsService.getActivePromotions());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<News>> getNewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(newsService.getNewsByUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<News>> searchNews(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        
        if (title != null) {
            return ResponseEntity.ok(newsService.searchNewsByTitle(title));
        } else if (content != null) {
            return ResponseEntity.ok(newsService.searchNewsByContent(content));
        } else if (startDate != null && endDate != null) {
            return ResponseEntity.ok(newsService.getNewsByDateRange(startDate, endDate));
        }
        
        return ResponseEntity.ok(newsService.getAllNews());
    }
} 