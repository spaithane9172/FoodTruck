package com.foodtruck.foodtruck.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodTruckModel {
    private String name;
    private String foodTruckName;
    private String closingTime;
    private String openingTime;
    private String status;
    private String type;
    private String email;
    private String password;
    private String cpassword;
    private String lat;
    private String Longi;
}
