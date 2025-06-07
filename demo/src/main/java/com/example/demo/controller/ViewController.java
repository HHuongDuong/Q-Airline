package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login.html")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register.html")
    public String register() {
        return "register.html";
    }

    @GetMapping({"/index.html", "/"})
    public String index() {
        return "index.html";
    }

    @GetMapping("/flights.html")
    public String flights() {
        return "flights.html";
    }

    @GetMapping("/news.html")
    public String news() {
        return "news.html";
    }

    @GetMapping("/tickets.html")
    public String tickets() {
        return "tickets.html";
    }

    @GetMapping("/flight-detail.html")
    public String flightDetail() {
        return "flight-detail.html";
    }

    @GetMapping("/seat-selection.html")
    public String seatSelection() {
        return "seat-selection.html";
    }

    @GetMapping("/news-detail.html")
    public String newsDetail() {
        return "news-detail.html";
    }

    @GetMapping("/flight.html")
    public String flight() {
        return "flight.html";
    }
} 