package com.example.HotelManagement.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Entity
@Data
public class Booking {
    @Id
    private Long bid;
    @NonNull
    private LocalDate checkInDate;
    @NonNull
    private LocalDate checkOutDate;
    private  int numOfAdults;
    private int totalGuest;
    @NonNull
    private String bookingCode;
    private Room room;
    private User roomUser;

}
