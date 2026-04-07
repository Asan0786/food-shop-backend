package com.fooddelivery.FoodHouse.io;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {

    private String name;
    private String description;
    private Double price;
    private String category;
}
