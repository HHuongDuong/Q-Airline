package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Ánh xạ rõ ràng các URL đến tên view Thymeleaf tương ứng
        registry.addViewController("/admin/admin-create-seat").setViewName("admin/admin-create-seat");
        registry.addViewController("/admin/admin-edit-seat").setViewName("admin/admin-edit-seat");
        registry.addViewController("/admin/dashboard").setViewName("admin/dashboard");
        registry.addViewController("/admin/admin-flights").setViewName("admin/admin-flights");
        registry.addViewController("/admin/delays").setViewName("admin/delays");
        registry.addViewController("/admin/admin-tickets").setViewName("admin/admin-tickets");
        registry.addViewController("/admin/admin-users").setViewName("admin/admin-users");
        registry.addViewController("/admin/admin-news").setViewName("admin/admin-news");
        registry.addViewController("/admin/admin-aircraft").setViewName("admin/admin-aircraft");
        registry.addViewController("/admin/admin-seats").setViewName("admin/admin-seats");

        // Các URL cho người dùng thông thường
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/flights").setViewName("flights");
        registry.addViewController("/news").setViewName("news");
        registry.addViewController("/news-detail").setViewName("news-detail");
        registry.addViewController("/tickets").setViewName("tickets");
        registry.addViewController("/ticket-detail").setViewName("ticket-detail");
        registry.addViewController("/flight-detail").setViewName("flight-detail");
        registry.addViewController("/seat-selection").setViewName("seat-selection");
        registry.addViewController("/").setViewName("index"); // Home page
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
}