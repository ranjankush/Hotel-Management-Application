package com.example.HotelManagement.controller;

import com.example.HotelManagement.dto.LoginRequest;
import com.example.HotelManagement.dto.Response;
import com.example.HotelManagement.model.User;
import com.example.HotelManagement.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        if (loginRequest == null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

