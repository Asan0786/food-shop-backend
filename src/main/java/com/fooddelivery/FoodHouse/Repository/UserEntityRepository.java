package com.fooddelivery.FoodHouse.Repository;

import com.fooddelivery.FoodHouse.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends MongoRepository<UserEntity,String> {

     Optional<UserEntity> findByEmail(String email);
}
