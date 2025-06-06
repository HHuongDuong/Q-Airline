package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewsDTO {
    private Long id;
    private String title;
    private String content;
    private String type;
    private LocalDateTime publishDate;
    private LocalDateTime expiryDate;
    private boolean active;
    private Long authorId;
    private String authorName;
} 