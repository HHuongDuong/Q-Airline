package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class FlightDelayRequestDTO {
    private Long flightId;
    private Integer delayDuration;
    private String reason;
    private String status;
}