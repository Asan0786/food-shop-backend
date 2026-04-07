package com.fooddelivery.FoodHouse.service;

import com.fooddelivery.FoodHouse.io.OrderRequest;
import com.fooddelivery.FoodHouse.io.OrderResponse;
import com.razorpay.RazorpayException;

import java.util.List;
import java.util.Map;

public interface OrderService {

   OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;

   void verifyPayment(Map<String,String> paymentData, String status);

    List<OrderResponse> getUserOrders(); //this method is going to return the orders to the loggedInusrs

    //if anyorder user is failed to complete the order(payment faild) the remove the order
    void removeOrder(String orderId);

    List<OrderResponse> getOrdersOfAllUsers(); //this method is going to return to the admin coz admin can see the ordrs of all users

    // admin can update the status of order
    void updateOrderStatus(String orderId, String status);


}
