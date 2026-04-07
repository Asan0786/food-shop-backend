package com.fooddelivery.FoodHouse.service;

import com.fooddelivery.FoodHouse.io.FoodRequest;
import com.fooddelivery.FoodHouse.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

     String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest request, MultipartFile file);

    List<FoodResponse> readFoods();

    FoodResponse readFood(String id);

    Boolean deleteFile(String fileName);

    void deleteFood(String id);
}
