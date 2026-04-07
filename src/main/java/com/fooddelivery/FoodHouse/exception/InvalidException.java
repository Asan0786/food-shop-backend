package com.fooddelivery.FoodHouse.exception;

public class InvalidException extends RuntimeException{

    public InvalidException(String mesg){
        super(mesg);
    }
}
