package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login.html")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping({"/index", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/flights")
    public String flights() {
        return "flights";
    }

    @GetMapping("/news")
    public String news() {
        return "news";
    }

    @GetMapping("/tickets")
    public String tickets() {
        return "tickets";
    }

    @GetMapping("/ticket-detail.html")
    public String ticketDetail() {  
        return "ticket-detail.html";
    }

    @GetMapping("/flight-detail")
    public String flightDetail() {
        return "flight-detail";
    }

    @GetMapping("/seat-selection")
    public String seatSelection() {
        return "seat-selection";
    }

    @GetMapping("/news-detail")
    public String newsDetail() {
        return "news-detail";
    }

    @GetMapping("/flight")
    public String flight() {
        return "flight";
    }
}