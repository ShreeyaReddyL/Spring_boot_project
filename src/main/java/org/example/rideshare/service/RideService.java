package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public RideResponse createRide(CreateRideRequest request, Long userId) {
        Ride ride = new Ride(userId, request.getPickupLocation(), request.getDropLocation());
        Ride savedRide = rideRepository.save(ride);
        return convertToResponse(savedRide);
    }

    public List<RideResponse> getAllPendingRides() {
        return rideRepository.findByStatus("REQUESTED")
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public RideResponse acceptRide(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!ride.getStatus().equals("REQUESTED")) {
            throw new BadRequestException("Ride must be in REQUESTED status to accept");
        }

        ride.setDriverId(driverId);
        ride.setStatus("ACCEPTED");
        Ride updatedRide = rideRepository.save(ride);
        return convertToResponse(updatedRide);
    }

    public RideResponse completeRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!ride.getStatus().equals("ACCEPTED")) {
            throw new BadRequestException("Ride must be in ACCEPTED status to complete");
        }

        ride.setStatus("COMPLETED");
        Ride updatedRide = rideRepository.save(ride);
        return convertToResponse(updatedRide);
    }

    public List<RideResponse> getUserRides(Long userId) {
        return rideRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private RideResponse convertToResponse(Ride ride) {
        return new RideResponse(
                ride.getId(),
                ride.getUserId(),
                ride.getDriverId(),
                ride.getPickupLocation(),
                ride.getDropLocation(),
                ride.getStatus(),
                ride.getCreatedAt()
        );
    }
}