package com.fooddelivery.FoodHouse.controller;

import com.fooddelivery.FoodHouse.io.OrderRequest;
import com.fooddelivery.FoodHouse.io.OrderResponse;
import com.fooddelivery.FoodHouse.service.OrderService;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000" , "http://localhost:5173"})
@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrderWithPayment(@RequestBody OrderRequest request) throws RazorpayException {

        OrderResponse response = orderService.createOrderWithPayment(request);
          return response;
    }

    @PostMapping("/verify")
    public void verifyPayment(@RequestBody Map<String, String > paymentData){
         orderService.verifyPayment(paymentData,"paid");
    }

    @GetMapping
    public List<OrderResponse> getUserOrders(){
        return orderService.getUserOrders();
    }

    @DeleteMapping("{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
    }

    //for admin panel
    @GetMapping("/all")
    public List<OrderResponse> getOrdersOfAllUsers(){
        return orderService.getOrdersOfAllUsers();
    }

    //for admin panel
    @PatchMapping("/status/{orderId}")
    public void updateOrderStatus(@PathVariable String orderId, @RequestParam String status){
        orderService.updateOrderStatus(orderId,status);
    }

}
