package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsRequestDTO {
    private String title;
    private String content;
    private String imageUrl;
}