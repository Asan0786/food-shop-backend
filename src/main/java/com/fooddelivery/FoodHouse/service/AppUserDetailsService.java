package com.fooddelivery.FoodHouse.service;

import com.fooddelivery.FoodHouse.Repository.UserEntityRepository;
import com.fooddelivery.FoodHouse.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor

public class AppUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      UserEntity user =   userEntityRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User Not found"));

      return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
