package com.example.demo.e2e;

import com.example.demo.entity.Seat;
import com.example.demo.enums.SeatType;
import com.example.demo.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeatSelectionE2ETest {

    private static final String BASE_URL = "http://localhost:";
    private static final String SEAT_CLASS = "class";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    public void testGetAvailableSeats() {
        String url = BASE_URL + port + "/api/seats/flight/1/available";
        ResponseEntity<Seat[]> response = restTemplate.getForEntity(url, Seat[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testGetSeatsByType() {
        String url = BASE_URL + port + "/api/seats/flight/1/type/" + SeatType.ECONOMY;
        ResponseEntity<Seat[]> response = restTemplate.getForEntity(url, Seat[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testGetSeatMap() {
        String url = BASE_URL + port + "/api/seats/flight/1/map";
        ResponseEntity<Seat[]> response = restTemplate.getForEntity(url, Seat[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
} 