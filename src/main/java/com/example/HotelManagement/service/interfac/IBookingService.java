package com.example.HotelManagement.service.interfac;

import com.example.HotelManagement.dto.Response;
import com.example.HotelManagement.model.Booking;
import com.example.HotelManagement.model.User;

public interface IBookingService {
    Response saveBooking(String roomId, String userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(String bookingId);
    Response getBookingsByUserId(String userId);


    Response sendBookingConfirmationEmail(User user,Booking booking);
}
