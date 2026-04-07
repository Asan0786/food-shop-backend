package com.fooddelivery.FoodHouse.service;

import org.springframework.security.core.Authentication;

public interface AuthenticatonFacade {

    Authentication getAuthentication();
}
