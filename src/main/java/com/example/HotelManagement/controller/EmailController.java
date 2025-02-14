package com.example.HotelManagement.controller;

import com.example.HotelManagement.model.Booking;
import com.example.HotelManagement.model.User;
import com.example.HotelManagement.repository.BookingRepository;
import com.example.HotelManagement.repository.UserRepository;
import com.example.HotelManagement.service.impl.EmailService;
import com.example.HotelManagement.service.interfac.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class EmailController {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private IEmailService emailService;

    @PostMapping("/send-email")
    public void sendEmail(@RequestParam String to,@RequestParam String subject, @RequestParam String message ){
        Optional<User> userOptional = userRepository.findByEmail(to);
        if (userOptional.isEmpty()) {
            logger.warn("User not found with email: {}", to);
            return;
        }
        User user = userOptional.get();

        Optional<Booking> bookingOptional = bookingRepository.findByBookingConfirmationCode(message);
        if (bookingOptional.isEmpty()) {
            logger.warn("Booking not found with confirmation code: {}", message);
            return;
        }
        Booking booking = bookingOptional.get();
        emailService.sendEmail(user.getEmail(),"Email from the Taj Hotel",booking.getBookingConfirmationCode());


    }

}
