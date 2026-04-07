package com.fooddelivery.FoodHouse.service;

import com.fooddelivery.FoodHouse.Repository.UserEntityRepository;
import com.fooddelivery.FoodHouse.entity.UserEntity;
import com.fooddelivery.FoodHouse.io.UserRequest;
import com.fooddelivery.FoodHouse.io.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {



    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticatonFacade authenticatonFacade;

//    public UserServiceImpl(PasswordEncoder passwordEncoder, UserEntityRepository userEntityRepository, AuthenticatonFacade authenticatonFacade) {
//        this.passwordEncoder = passwordEncoder;
//        this.userEntityRepository = userEntityRepository;
//        this.authenticatonFacade = authenticatonFacade;
//    }


    @Override
    public UserResponse registerNewUser(UserRequest userRequest) {

         UserEntity newUser = convertToEntity(userRequest);

          newUser = userEntityRepository.save(newUser);

          return  convertToResponse(newUser);
    }

    @Override
    public String findByUserId() {
          //get logged in user
         String loggedUserEmai = authenticatonFacade.getAuthentication().getName();
        UserEntity loggedInUser =  userEntityRepository.findByEmail(loggedUserEmai).orElseThrow(()-> new UsernameNotFoundException("user not found"));
        return loggedInUser.getEmail();
    }

    private UserResponse convertToResponse(UserEntity registeredUser){
           return  UserResponse.builder().
                   id(registeredUser.getId()).
                   name(registeredUser.getName())
                   .email(registeredUser.getEmail())
                   .build();

    }

    private UserEntity convertToEntity(UserRequest userRequest){
         return UserEntity.builder()
                 .email(userRequest.getEmail())
                 .name(userRequest.getName())
                 .password((passwordEncoder.encode(userRequest.getPassword()))).build();
    }

}
