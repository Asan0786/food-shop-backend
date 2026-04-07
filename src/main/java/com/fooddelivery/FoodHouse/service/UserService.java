package com.fooddelivery.FoodHouse.service;


import com.fooddelivery.FoodHouse.io.UserRequest;
import com.fooddelivery.FoodHouse.io.UserResponse;
import org.springframework.stereotype.Service;


public interface UserService {

    UserResponse registerNewUser(UserRequest userRequest);

      String findByUserId();
}
