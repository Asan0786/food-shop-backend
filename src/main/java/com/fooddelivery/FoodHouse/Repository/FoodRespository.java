package com.fooddelivery.FoodHouse.Repository;

import com.fooddelivery.FoodHouse.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRespository extends MongoRepository<FoodEntity,String> {


}
