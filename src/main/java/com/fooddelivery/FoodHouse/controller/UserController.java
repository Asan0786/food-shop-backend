package com.fooddelivery.FoodHouse.controller;


import com.fooddelivery.FoodHouse.io.UserRequest;
import com.fooddelivery.FoodHouse.io.UserResponse;
import com.fooddelivery.FoodHouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {


       @Autowired
      private UserService userService;

       @PostMapping("/register")
       @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerNewUser(@RequestBody UserRequest request) {
           return userService.registerNewUser(request);
       }
}
