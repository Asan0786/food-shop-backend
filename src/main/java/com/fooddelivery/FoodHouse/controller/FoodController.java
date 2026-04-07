package com.fooddelivery.FoodHouse.controller;

import com.fooddelivery.FoodHouse.exception.InvalidException;
import com.fooddelivery.FoodHouse.io.FoodRequest;
import com.fooddelivery.FoodHouse.io.FoodResponse;
import com.fooddelivery.FoodHouse.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.thirdparty.jackson.core.JsonProcessingException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
@CrossOrigin("*")
public class FoodController {


       private final FoodService foodService;

       @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FoodResponse addFood(@RequestPart("food") String foodString, @RequestPart("file") MultipartFile file){

        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;

        try{
            request =  objectMapper.readValue(foodString,FoodRequest.class);
        }catch(Exception ex){
            ex.printStackTrace();
              throw new RuntimeException("Invalid Json formate" +ex.getMessage());

        }
        FoodResponse response = foodService.addFood(request,file);
        return response;
    }

    @GetMapping
    public List<FoodResponse> readFoods(){
         List<FoodResponse> response= foodService.readFoods();
         return response;
    }

    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id){
          return foodService.readFood(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id){
        foodService.deleteFood(id);
    }
}
