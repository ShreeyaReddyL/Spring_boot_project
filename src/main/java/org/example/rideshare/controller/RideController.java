package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping("/rides")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        RideResponse response = rideService.createRide(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<RideResponse> completeRide(@PathVariable Long rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/rides")
    public ResponseEntity<List<RideResponse>> getUserRides() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        List<RideResponse> rides = rideService.getUserRides(userId);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<RideResponse>> getPendingRideRequests() {
        List<RideResponse> rides = rideService.getAllPendingRides();
        return ResponseEntity.ok(rides);
    }

    @PostMapping("/driver/rides/{rideId}/accept")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable Long rideId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long driverId = Long.parseLong(auth.getName());
        RideResponse response = rideService.acceptRide(rideId, driverId);
        return ResponseEntity.ok(response);
    }
}