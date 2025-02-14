package com.example.HotelManagement.service.impl;

import com.example.HotelManagement.dto.*;
import com.example.HotelManagement.exception.OurException;
import com.example.HotelManagement.model.*;
import com.example.HotelManagement.repository.BookingRepository;
import com.example.HotelManagement.repository.RoomRepository;
import com.example.HotelManagement.repository.UserRepository;
import com.example.HotelManagement.service.interfac.IBookingService;
import com.example.HotelManagement.service.interfac.IRoomService;
import com.example.HotelManagement.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService implements IBookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private IRoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);


    @Override
    public Response saveBooking(String roomId, String userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must come before check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Booking> existingBookings = room.getBookings();
            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("Room not Available for the selected date range");
            }
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);

            //save booking
            Booking savedBooking = bookingRepository.save(bookingRequest);


            // Add the booking to the user's bookings list
            List<Booking> userBookings = user.getBookings();
            userBookings.add(savedBooking);
            user.setBookings(userBookings);
            userRepository.save(user);


            // Add the booking to the room's bookings list
            List<Booking> roomBookings = room.getBookings();
            roomBookings.add(savedBooking);
            room.setBookings(roomBookings);
            roomRepository.save(room);


            // Send booking confirmation email
            Response emailResponse = sendBookingConfirmationEmail(user, savedBooking);

            if (emailResponse.isSuccess()) {
                response.setStatusCode(200);
                response.setMessage("Booking created successfully. Confirmation email sent.");
            } else {
                response.setStatusCode(500);
                response.setMessage("Booking created, but email failed to send.");
            }
            response.setBookingConfirmationCode(bookingConfirmationCode);


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a  booking " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking Not Found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setBooking(bookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting booking by confirmation code " + e.getMessage());
        }
        return response;
    }
    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setBookingList(bookingDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(String bookingId) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking Not Found"));

            // Remove the booking from the associated user
            User user = booking.getUser();
            if (user != null) {
                user.getBookings().removeIf(b -> b.getId().equals(bookingId));
                userRepository.save(user);
            }

            // Remove the booking from the associated room
            Room room = booking.getRoom();
            if (room != null) {
                room.getBookings().removeIf(b -> b.getId().equals(bookingId));
                roomRepository.save(room);
            }

            // Delete the booking
            bookingRepository.deleteById(bookingId);

            response.setMessage("successful");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error cancelling a booking " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getBookingsByUserId(String userId) {
        Response response = new Response();

        try {
            // Fetch the user from the database
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User Not Found"));

            // Map the user entity to UserDTO, including nested bookings and rooms
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);

            // Set the response fields
            response.setStatusCode(200);
            response.setMessage("User bookings retrieved successfully");
            response.setUser(userDTO); // Attach the UserDTO to the response

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving bookings by user ID: " + e.getMessage());
        }

        return response;
    }


public Response sendBookingConfirmationEmail(User user, Booking booking) {
    Response response = new Response();
    try {
        String subject = "Booking Confirmation Message- The Taj Hotel";
        String emailContent = String.format(
                "Dear %s,\n\nYour booking at The Taj Hotel is confirmed.\nBooking Code: %s\nCheck-in Date: %s\nCheck-out Date: %s\n\nThank you!",
                user.getName(), booking.getBookingConfirmationCode(), booking.getCheckInDate(), booking.getCheckOutDate());

        emailService.sendEmail(user.getEmail(), subject, emailContent);
        logger.info("Booking confirmation email sent to {}", user.getEmail());

        response.setStatusCode(200);
        response.setMessage("Email sent successfully.");
        response.setSuccess(true);
    } catch (Exception e) {
        logger.error("Failed to send email to {}", user.getEmail(), e);
        response.setStatusCode(500);
        response.setMessage("Failed to send email.");
        response.setSuccess(false);
    }
    return response;
}



    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }



}
