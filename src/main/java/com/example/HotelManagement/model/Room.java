package com.example.HotelManagement.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
public class Room {
    @Id
    private Long rid;
    @NonNull
    private String roomType;
    @NonNull
    private Double roomPrice;
    @NonNull
    private String roomPhUrl;
    @NonNull
    private String roomDesc;
    private List<Booking> booking;
}
