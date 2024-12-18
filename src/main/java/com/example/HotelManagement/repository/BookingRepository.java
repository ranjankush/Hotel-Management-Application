package com.example.HotelManagement.repository;

import com.example.HotelManagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
}
