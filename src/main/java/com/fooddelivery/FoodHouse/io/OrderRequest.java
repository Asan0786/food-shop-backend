package com.fooddelivery.FoodHouse.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderRequest {


    private List<OrderItem> orderItemList;
    private String userAddress;
    private double amount;
    private String email;
    private String phoneNumber;
    private String orderStatus;



}
