package com.fooddelivery.FoodHouse.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AuthenticationFacadeImpl implements AuthenticatonFacade{
    @Override
    public Authentication getAuthentication() { //return logged in user
       return SecurityContextHolder.getContext().getAuthentication();
    }
}
