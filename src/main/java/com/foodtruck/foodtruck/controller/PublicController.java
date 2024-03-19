package com.foodtruck.foodtruck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class PublicController {

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/registerFoodTruck")
    public String registerFoodTruck() {
        return "registerFoodTruck";
    }

    @RequestMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @RequestMapping("/registerUser")
    public String registerUser() {
        return "registerUser";
    }

}
