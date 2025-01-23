package com.example.HotelManagement.repository;

import com.example.HotelManagement.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends MongoRepository<Booking,String> {

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    //checkInDate is less than or equal to the checkOutDate and while the checkOutDate is greater than or equal to the checkInDate
    @Query("{ 'checkInDate': { $lte: ?1 }, 'checkOutDate': { $gte: ?0 } }")
    List<Booking> findBookingsByDateRange(LocalDate checkInDate, LocalDate checkOutDate);
}
