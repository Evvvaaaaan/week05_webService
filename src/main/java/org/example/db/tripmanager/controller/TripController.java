package org.example.db.tripmanager.controller;


import org.example.db.tripmanager.dto.TripRequest;
import org.example.db.tripmanager.dto.TripResponse;
import org.example.db.tripmanager.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/{id}")
    public TripResponse findById(@PathVariable Long id) {
        return tripService.findById(id);
    }
    @GetMapping
    public List<TripResponse> findAll() {
        return tripService.findAll();
    }
    @PutMapping("/{id}")
    public TripResponse update(
            @PathVariable Long id,
            @RequestBody TripRequest request) {
        return tripService.update(id, request);
    }

    @PostMapping
    public ResponseEntity<TripResponse> create (
        @RequestBody TripRequest request) {
        TripResponse response = tripService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        }
}
