package com.fooddelivery.FoodHouse.Repository;

import com.fooddelivery.FoodHouse.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends MongoRepository<CartEntity,String> {

   Optional<CartEntity> findByUserId(String foodId);
    void deleteFoodByUserId(String foodId);
}
