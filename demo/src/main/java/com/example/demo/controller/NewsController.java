package com.example.demo.controller;

import com.example.demo.entity.News;
import com.example.demo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping
    public ResponseEntity<News> createNews(@RequestBody News news) {
        return ResponseEntity.ok(newsService.createNews(news));
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        news.setId(id);
        return ResponseEntity.ok(newsService.updateNews(news));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<News>> getNewsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(newsService.getNewsByDateRange(start, end));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<News>> searchNewsByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(newsService.searchNewsByTitle(keyword));
    }

    @GetMapping("/search/content")
    public ResponseEntity<List<News>> searchNewsByContent(@RequestParam String keyword) {
        return ResponseEntity.ok(newsService.searchNewsByContent(keyword));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<News>> getRecentNews(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(newsService.getRecentNews(date));
    }
} 