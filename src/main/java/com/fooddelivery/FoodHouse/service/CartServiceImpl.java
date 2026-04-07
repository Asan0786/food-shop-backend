package com.fooddelivery.FoodHouse.service;

import com.fooddelivery.FoodHouse.Repository.CartRepository;

import com.fooddelivery.FoodHouse.entity.CartEntity;
import com.fooddelivery.FoodHouse.io.CartRequest;
import com.fooddelivery.FoodHouse.io.CartResponse;
import lombok.AllArgsConstructor;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{


    private final UserService userService;


    private final CartRepository cartRepository;

    @Override
    public CartResponse addFood(CartRequest request) {
        String loggedInUserId =  userService.findByUserId();

         Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity cart = cartOptional.orElseGet(()-> new CartEntity(loggedInUserId,new HashMap<>()));

        Map<String , Integer> cartItems =  cart.getItems();
        cartItems.put(request.getFoodId(),cartItems.getOrDefault(request.getFoodId(),0) + 1);

        cart.setItems(cartItems);

        cart = cartRepository.save(cart);

        return convertToResponse(cart);
    }

    @Override
    public CartResponse getFood() {

          String loggedInUserId =  userService.findByUserId();
           CartEntity entity =   cartRepository.findByUserId(loggedInUserId).orElse(new CartEntity(loggedInUserId,new HashMap<>()));
           return convertToResponse(entity);
    }

    @Override
    public void clearCart() {
      String loggedInUserId  = userService.findByUserId();
       cartRepository.deleteFoodByUserId(loggedInUserId);
    }

    @Override
    public CartResponse remove(CartRequest request) {
        String loggedInUser = userService.findByUserId();
         CartEntity cart = cartRepository.findByUserId(loggedInUser).orElse(new CartEntity(loggedInUser,new HashMap<>()));

         Map<String,Integer> cartItems = cart.getItems();

         if(cartItems.containsKey(request.getFoodId())){
             int currentItemQuantity = cartItems.get(request.getFoodId());

             if(currentItemQuantity > 0) {
                 cartItems.put(request.getFoodId(),currentItemQuantity - 1);
             }else{
                 cartItems.remove(request.getFoodId());
             }
         }
        cart =  cartRepository.save(cart);

        return convertToResponse(cart);
    }

    private CartResponse convertToResponse(CartEntity cart){
       return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems())
                .build();
    }

}
