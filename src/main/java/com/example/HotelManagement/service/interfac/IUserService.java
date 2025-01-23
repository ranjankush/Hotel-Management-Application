package com.example.HotelManagement.service.interfac;

import com.example.HotelManagement.dto.LoginRequest;
import com.example.HotelManagement.dto.Response;
import com.example.HotelManagement.model.User;
import com.example.HotelManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface IUserService {


    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
