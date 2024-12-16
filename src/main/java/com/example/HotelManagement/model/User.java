package com.example.HotelManagement.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
public class User {
    @Id
    private Long uid;
    @NonNull
    private String  email;
    @NonNull
    private String name;
    @NonNull
    private String pNo;
    @NonNull
    private String password;
    @NonNull
    private String role;
    private List<Booking> Booking;
}
