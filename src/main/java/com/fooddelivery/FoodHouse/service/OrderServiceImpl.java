package com.fooddelivery.FoodHouse.service;


import com.fooddelivery.FoodHouse.Repository.CartRepository;
import com.fooddelivery.FoodHouse.Repository.OrderRepository;
import com.fooddelivery.FoodHouse.entity.OrderEntity;
import com.fooddelivery.FoodHouse.io.OrderRequest;
import com.fooddelivery.FoodHouse.io.OrderResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Value("${razorpay_key_Id}")
    private String razorpay_key_Id;
     @Value("${razorpay_key_secret}")
    private String razorpay_key_secret;

     @Autowired
     private UserService userService;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
          OrderEntity newOrder =  converToEntity(request);
           newOrder =  orderRepository.save(newOrder);


           //create RazorPay payment order
        RazorpayClient razorpayClient = new RazorpayClient(razorpay_key_Id,razorpay_key_secret);
        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", request.getAmount()*100);
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture",1);

        //Once the client is built, it is typically used to create an order on the backend to initiate the payment process
       Order razorpayOrder = razorpayClient.Orders.create(orderRequest);
       newOrder.setRazorpayOrderId(razorpayOrder.get("id"));
       String loggedInUserId = userService.findByUserId();
       newOrder.setUserId(loggedInUserId);
       newOrder = orderRepository.save(newOrder);

      return converToResponse(newOrder);


    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");

      OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(()-> new RuntimeException("Order not found"));
       existingOrder.setPaymentStatus(status);
       existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
       existingOrder.setRazorPaymentId(paymentData.get("razorpay_payment_id"));
       orderRepository.save(existingOrder);

        //if user made the payment successfully then remove the item from the cart
              if("paid".equalsIgnoreCase(status)){
                  cartRepository.deleteById(existingOrder.getId());
              }
    }

    @Override
    public List<OrderResponse> getUserOrders() {
          String loggedInUser = userService.findByUserId();
          List<OrderEntity> list =orderRepository.findByUserId(loggedInUser);

          return list.stream().map((entity)-> converToResponse(entity)).collect(Collectors.toList());

    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        List<OrderEntity> list = orderRepository.findAll();
        return list.stream().map((entity)-> converToResponse(entity)).collect(Collectors.toList());

    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
      OrderEntity entity =  orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));
      entity.setOrderStatus(status);
      orderRepository.save(entity);

    }



    private OrderResponse converToResponse(OrderEntity newOrder) {

       return OrderResponse.builder()
                .id(newOrder.getId())
                .amount(newOrder.getAmount())
                .userAddress(newOrder.getUserAddress())
                .userId(newOrder.getUserId())
                .razorpayOrderId(newOrder.getRazorpayOrderId())
                .paymentStatus(newOrder.getPaymentStatus())
                .orderStatus(newOrder.getOrderStatus())
               .email(newOrder.getEmail())
               .phoneNumber(newOrder.getPhoneNumber())
               .orderedItems(newOrder.getOrderedItems())
                .build();
    }

    private OrderEntity converToEntity(OrderRequest request){
       return OrderEntity.builder()
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .orderedItems(request.getOrderItemList())
               .email(request.getEmail())
               .phoneNumber(request.getPhoneNumber())
               .orderStatus(request.getOrderStatus())
                .build();
    }
}
