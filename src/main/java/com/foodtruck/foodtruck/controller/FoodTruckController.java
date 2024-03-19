package com.foodtruck.foodtruck.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.foodtruck.foodtruck.config.CustomUserDetails;
import com.foodtruck.foodtruck.entity.FoodtruckEntity;
import com.foodtruck.foodtruck.entity.MenuEntity;
import com.foodtruck.foodtruck.model.FoodTruckModel;
import com.foodtruck.foodtruck.service.FoodTruckService;
import com.foodtruck.foodtruck.service.MenuService;

@Controller
@RequestMapping("/foodTruck")
public class FoodTruckController {

    @Autowired
    FoodTruckService foodTruckService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MenuService menuService;

    @RequestMapping("/saveNewFoodTruck")
    public String saveNewFoodTruck(FoodTruckModel foodTruckModel) {
        if (foodTruckModel.getPassword().equals(foodTruckModel.getCpassword()) && foodTruckModel.getName().length() >= 3
                && foodTruckModel.getFoodTruckName().length() >= 3 && foodTruckModel.getPassword().length() >= 8
                && foodTruckModel.getEmail().length() > 9) {

            FoodtruckEntity foodtruckEntity = new FoodtruckEntity();
            foodtruckEntity.setName(foodTruckModel.getName());
            foodtruckEntity.setFoodTruckName(foodTruckModel.getFoodTruckName());
            foodtruckEntity.setEmail(foodTruckModel.getEmail());
            foodtruckEntity.setPassword(passwordEncoder.encode(foodTruckModel.getPassword()));

            foodTruckService.saveNewFoodTruck(foodtruckEntity);
            return "redirect:/";
        }
        return "redirect:/registerFoodTruck";
    }

    @RequestMapping("/foodTruckDashboard")
    public String foodTruckDashboard(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        FoodtruckEntity foodtruckEntity = foodTruckService.findFoodTruckByEmail(user.getUsername());
        foodtruckEntity.setId(null);
        foodtruckEntity.setPassword(null);
        foodtruckEntity.setRole(null);
        model.addAttribute("foodtruck", foodtruckEntity);
        return "foodTruck";
    }

    @RequestMapping("/addMenu")
    public String addMenuImg(@RequestParam("menuImg") MultipartFile file, @RequestParam("email") String email)
            throws IOException {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        FoodtruckEntity foodtruckEntity = foodTruckService.findFoodTruckByEmail(email);
        foodtruckEntity.getMenuEntity().add(menuEntity);
        foodtruckEntity.setMenuEntity(foodtruckEntity.getMenuEntity());
        foodTruckService.updateFoodTruck(foodtruckEntity);
        return "redirect:/foodTruck/foodTruckDashboard";
    }

    @RequestMapping("/deleteMenu/{id}")
    public String deleteMenu(@PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return "redirect:/foodTruck/foodTruckDashboard";
    }
}
