package com.fooddelivery.FoodHouse.controller;


import com.fooddelivery.FoodHouse.entity.CartEntity;
import com.fooddelivery.FoodHouse.io.CartRequest;
import com.fooddelivery.FoodHouse.io.CartResponse;
import com.fooddelivery.FoodHouse.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

     private final CartService cartService;

     @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request){
       String foodId =  request.getFoodId();
        System.out.println("Food Id is ...."+foodId);
       if(foodId == null || foodId.isEmpty()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"food not found");
       }
       return cartService.addFood(request);

    }

    @GetMapping
    public CartResponse getCart(){
        return cartService.getFood();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(){
         cartService.clearCart();
    }


    @PostMapping("/remove")
    public CartResponse remove(@RequestBody CartRequest request){
         String foodId = request.getFoodId();
         if(foodId == null || foodId.isEmpty()){
              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food Not found");
         }
         return cartService.remove(request);
    }
}
