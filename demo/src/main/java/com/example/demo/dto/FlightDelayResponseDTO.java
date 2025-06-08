package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class FlightDelayResponseDTO {
    private Long id;
    private String flightNumber;
    private String route;
    private Integer delayDuration;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}