package org.example.rideshare.repository;

import org.example.rideshare.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByStatus(String status);

    List<Ride> findByUserId(Long userId);
}