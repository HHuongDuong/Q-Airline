package com.example.demo.dto;

import com.example.demo.dto.PassengerDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestDTO {
    private Long flightId;
    private String seatNumber;
    private PassengerDTO passenger;
}
