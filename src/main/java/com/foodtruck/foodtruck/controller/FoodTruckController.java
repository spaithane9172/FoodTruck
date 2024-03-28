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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foodtruck.foodtruck.config.CustomUserDetails;
import com.foodtruck.foodtruck.entity.FoodtruckEntity;
import com.foodtruck.foodtruck.entity.MenuEntity;
import com.foodtruck.foodtruck.entity.UserEntity;
import com.foodtruck.foodtruck.model.FoodTruckModel;
import com.foodtruck.foodtruck.service.FoodTruckService;
import com.foodtruck.foodtruck.service.MenuService;
import com.foodtruck.foodtruck.service.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/foodTruck")
public class FoodTruckController {

    @Autowired
    FoodTruckService foodTruckService;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MenuService menuService;

    @RequestMapping("/saveNewFoodTruck")
    public String saveNewFoodTruck(FoodTruckModel foodTruckModel, RedirectAttributes m,
            @RequestParam("img") MultipartFile img) throws IOException {
        try {
            UserEntity userEntit = userServiceImpl.findUser(foodTruckModel.getEmail());
            FoodtruckEntity foodtruck = foodTruckService.findFoodTruckByEmail(foodTruckModel.getEmail());
            if (userEntit == null && foodtruck == null) {
                if (foodTruckModel.getPassword().equals(foodTruckModel.getCpassword())
                        && foodTruckModel.getName().length() >= 3
                        && foodTruckModel.getFoodTruckName().length() >= 3 &&
                        foodTruckModel.getPassword().length() >= 8
                        && foodTruckModel.getEmail().length() > 9) {

                    FoodtruckEntity foodtruckEntity = new FoodtruckEntity();
                    foodtruckEntity.setName(foodTruckModel.getName());
                    foodtruckEntity.setFoodTruckName(foodTruckModel.getFoodTruckName());
                    foodtruckEntity.setEmail(foodTruckModel.getEmail());
                    foodtruckEntity.setPassword(passwordEncoder.encode(foodTruckModel.getPassword()));
                    foodtruckEntity.setFoodTruckImage(Base64.getEncoder().encodeToString(img.getBytes()));

                    foodTruckService.saveNewFoodTruck(foodtruckEntity);
                    return "redirect:/";
                }
                m.addFlashAttribute("error", "please enter correct details");
                return "redirect:/public/registerFoodTruck";
            } else {
                m.addFlashAttribute("error", "Email already taken");
                return "redirect:/public/registerFoodTruck";

            }

        } catch (Exception e) {
            m.addAttribute("error", "Something Wrong Try Again");
            return "redirect:/public/registerFoodTruck";

        }

    }

    @RequestMapping("/updateFoodtruckImage")
    public String updateFoodtruckImage(@RequestParam("img") MultipartFile img,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        try {
            FoodtruckEntity foodtruckEntity = foodTruckService.findFoodTruckByEmail(customUserDetails.getUsername());
            foodtruckEntity.setFoodTruckImage(Base64.getEncoder().encodeToString(img.getBytes()));
            foodTruckService.updateFoodTruck(foodtruckEntity);
            return "redirect:/foodTruck/foodTruckDashboard";
        } catch (Exception e) {
            return "redirect:/foodTruck/foodTruckDashboard";
        }
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
    public String addMenuImg(@RequestParam("menuImg") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails customUserDetails, Model m)
            throws IOException {
        try {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            FoodtruckEntity foodtruckEntity = foodTruckService.findFoodTruckByEmail(customUserDetails.getUsername());
            foodtruckEntity.getMenuEntity().add(menuEntity);
            foodtruckEntity.setMenuEntity(foodtruckEntity.getMenuEntity());
            foodTruckService.updateFoodTruck(foodtruckEntity);
            return "redirect:/foodTruck/foodTruckDashboard";

        } catch (Exception e) {
            m.addAttribute("error", "Something Wrong Try Again");
            return "redirect:/foodTruck/foodTruckDashboard";
        }

    }

    @RequestMapping("/deleteMenu/{id}")
    public String deleteMenu(@PathVariable("id") Long id, Model m) {
        try {
            menuService.deleteMenu(id);
            return "redirect:/foodTruck/foodTruckDashboard";
        } catch (Exception e) {
            m.addAttribute("error", "Something Wrong Try Again");
            return "redirect:/foodTruck/foodTruckDashboard";
        }
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam("lat") String lat, @RequestParam("long") String longi,
            @AuthenticationPrincipal CustomUserDetails customUserDetails, Model m) {
        try {
            FoodtruckEntity foodtruckEntity = foodTruckService.findFoodTruckByEmail(customUserDetails.getUsername());
            foodtruckEntity.setLat(Double.parseDouble(lat));
            foodtruckEntity.setLongi(Double.parseDouble(longi));
            foodTruckService.updateFoodTruck(foodtruckEntity);
            return "redirect:/foodTruck/foodTruckDashboard";
        } catch (Exception e) {
            m.addAttribute("error", "Something Wrong Try Again");
            return "redirect:/foodTruck/foodTruckDashboard";
        }

    }

    @RequestMapping("/setClosingTime")
    public String setClosingTime(@RequestParam("closingTime") String closingTime,
            @AuthenticationPrincipal CustomUserDetails customUserDetails, Model m) {
        try {
            FoodtruckEntity foodtruckEntity = foodTruckService.findFoodTruckByEmail(customUserDetails.getUsername());
            foodtruckEntity.setClosingTime(closingTime);
            foodTruckService.updateFoodTruck(foodtruckEntity);
            return "redirect:/foodTruck/foodTruckDashboard";
        } catch (Exception e) {
            m.addAttribute("error", "Something Wrong Try Again");
            return "redirect:/foodTruck/foodTruckDashboard";
        }

    }
}
