package com.example.HotelManagement.service.interfac;

import com.example.HotelManagement.dto.Response;
import com.example.HotelManagement.model.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
