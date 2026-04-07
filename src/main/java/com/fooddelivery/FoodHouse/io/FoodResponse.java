package com.fooddelivery.FoodHouse.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    @Id
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private Double price;
    private String category;
}

