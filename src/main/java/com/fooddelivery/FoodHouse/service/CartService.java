package com.fooddelivery.FoodHouse.service;

import com.fooddelivery.FoodHouse.entity.CartEntity;
import com.fooddelivery.FoodHouse.io.CartRequest;
import com.fooddelivery.FoodHouse.io.CartResponse;

import java.util.Optional;

public interface CartService {

    CartResponse addFood(CartRequest request);

    CartResponse getFood();

    void clearCart();

    CartResponse remove(CartRequest request);


}
