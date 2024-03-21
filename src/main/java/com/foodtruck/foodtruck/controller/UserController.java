package com.foodtruck.foodtruck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.foodtruck.foodtruck.calculations.FindDistance;
import com.foodtruck.foodtruck.calculations.SortFoodTrucksByDistance;
import com.foodtruck.foodtruck.config.CustomUserDetails;
import com.foodtruck.foodtruck.entity.FoodtruckEntity;
import com.foodtruck.foodtruck.entity.UserEntity;
import com.foodtruck.foodtruck.model.UserModel;
import com.foodtruck.foodtruck.service.FoodTruckServiceImpl;
import com.foodtruck.foodtruck.service.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    SortFoodTrucksByDistance sortFoodTrucksByDistance;

    @Autowired
    FindDistance findDistance;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    FoodTruckServiceImpl foodTruckServiceImpl;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/saveNewUser")
    public String saveNewUser(UserModel userModel) {
        if (userModel.getPassword().equals(userModel.getCpassword()) && userModel.getName().length() >= 3
                && userModel.getEmail().length() >= 9) {
            UserEntity userEntity = new UserEntity();
            userEntity.setName(userModel.getName());
            userEntity.setEmail(userModel.getEmail());
            userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
            userServiceImpl.saveNewUser(userEntity);
            return "redirect:/public/";
        }
        return "redirect:/public/registerUser";
    }

    @RequestMapping("/userDashboard")
    public String userDashboard(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        UserEntity u = userServiceImpl.findUser(user.getUsername());
        List<FoodtruckEntity> foodtruckEntity = foodTruckServiceImpl.getAllFoodTrucksNearMe();

        for (int i = 0; i < foodtruckEntity.size(); i++) {
            foodtruckEntity.get(i).setId(null);
            foodtruckEntity.get(i).setPassword(null);
            foodtruckEntity.get(i).setRole(null);
            foodtruckEntity.get(i).setDistance(findDistance.calculateDistance(u.getLat(), u.getLongi(),
                    foodtruckEntity.get(i).getLat(), foodtruckEntity.get(i).getLongi()));
        }
        foodtruckEntity = sortFoodTrucksByDistance.sortObject(foodtruckEntity);
        u.setId(null);
        u.setPassword(null);
        u.setRole(null);
        model.addAttribute("user", u);
        model.addAttribute("foodtrucks", foodtruckEntity);
        return "users";
    }

    @RequestMapping("/getUserLocation")
    public String getUserLocation(@RequestParam("lat") String lat, @RequestParam("long") String longi,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserEntity userEntity = userServiceImpl.findUser(customUserDetails.getUsername());
        userEntity.setLat(Double.parseDouble(lat));
        userEntity.setLongi(Double.parseDouble(longi));
        userServiceImpl.updateUser(userEntity);
        return "redirect:/user/userDashboard";
    }

}
