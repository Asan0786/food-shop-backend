package com.fooddelivery.FoodHouse.Repository;

import com.fooddelivery.FoodHouse.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity,String> {

   List<OrderEntity> findByUserId(String userId);
   Optional<OrderEntity> findByRazorpayOrderId(String razorpayOrderId);
}
