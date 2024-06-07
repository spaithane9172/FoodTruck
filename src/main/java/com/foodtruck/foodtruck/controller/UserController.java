package com.foodtruck.foodtruck.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.foodtruck.foodtruck.calculations.FindDistance;
import com.foodtruck.foodtruck.calculations.SortFoodTrucksByDistance;
import com.foodtruck.foodtruck.config.CustomUserDetails;
import com.foodtruck.foodtruck.entity.FoodtruckEntity;
import com.foodtruck.foodtruck.entity.FoodtruckFeedbacksEntity;
import com.foodtruck.foodtruck.entity.MenuEntity;
import com.foodtruck.foodtruck.entity.UserEntity;
import com.foodtruck.foodtruck.model.FeedbackModel;
import com.foodtruck.foodtruck.model.UserModel;
import com.foodtruck.foodtruck.service.FoodTruckServiceImpl;
import com.foodtruck.foodtruck.service.MenuListServiceImpl;
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

    @Autowired
    MenuListServiceImpl menuListServiceImpl;

    @RequestMapping("/saveNewUser")
    public String saveNewUser(UserModel userModel, RedirectAttributes m) {
        UserEntity u = userServiceImpl.findUser(userModel.getEmail());
        FoodtruckEntity f = foodTruckServiceImpl.findFoodTruckByEmail(userModel.getEmail());
        if (u == null && f == null) {
            if (userModel.getPassword().equals(userModel.getCpassword())
                    && userModel.getName().length() >= 3
                    && userModel.getEmail().length() >= 9) {
                UserEntity userEntity = new UserEntity();
                userEntity.setName(userModel.getName());
                userEntity.setEmail(userModel.getEmail());
                userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
                userServiceImpl.saveNewUser(userEntity);
                m.addFlashAttribute("error", "Registration Successful, LogIn");

                return "redirect:/public/registerUser";
            } else {
                m.addFlashAttribute("error", "Please Enter Correct details");
            }
        } else {
            m.addFlashAttribute("error", "Email id already taken");
        }

        return "redirect:/public/registerUser";
    }

    @RequestMapping("/userDashboard")
    public String userDashboard(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        UserEntity u = userServiceImpl.findUser(user.getUsername());
        List<FoodtruckEntity> openFoodtruckEntity = foodTruckServiceImpl.getAllOpenFoodTrucksNearMe();
        List<FoodtruckEntity> closedFoodtruckEntity = foodTruckServiceImpl.getAllClosedFoodTrucksNearMe();
        List<FoodtruckEntity> foodtruckEntity = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try {
            for (int i = 0; i < openFoodtruckEntity.size(); i++) {
                openFoodtruckEntity.get(i).setId(null);
                openFoodtruckEntity.get(i).setPassword(null);
                openFoodtruckEntity.get(i).setRole(null);
                if (u.getLat() != null && u.getLongi() != null && closedFoodtruckEntity.get(i).getLat() != null
                        && closedFoodtruckEntity.get(i).getLongi() != null) {
                    openFoodtruckEntity.get(i)
                            .setDistance(Double.parseDouble(
                                    decimalFormat.format(findDistance.calculateDistance(u.getLat(), u.getLongi(),
                                            openFoodtruckEntity.get(i).getLat(),
                                            openFoodtruckEntity.get(i).getLongi()))));
                }
            }
            openFoodtruckEntity = sortFoodTrucksByDistance.sortObject(openFoodtruckEntity);

            for (int i = 0; i < closedFoodtruckEntity.size(); i++) {
                closedFoodtruckEntity.get(i).setId(null);
                closedFoodtruckEntity.get(i).setPassword(null);
                closedFoodtruckEntity.get(i).setRole(null);
                if (u.getLat() != null && u.getLongi() != null && closedFoodtruckEntity.get(i).getLat() != null
                        && closedFoodtruckEntity.get(i).getLongi() != null) {
                    closedFoodtruckEntity.get(i)
                            .setDistance(Double.parseDouble(
                                    decimalFormat.format(findDistance.calculateDistance(u.getLat(), u.getLongi(),
                                            closedFoodtruckEntity.get(i).getLat(),
                                            closedFoodtruckEntity.get(i).getLongi()))));
                }
            }

            closedFoodtruckEntity = sortFoodTrucksByDistance.sortObject(closedFoodtruckEntity);
            foodtruckEntity = openFoodtruckEntity;
            foodtruckEntity.addAll(closedFoodtruckEntity);
        } catch (Exception e) {
            foodtruckEntity = openFoodtruckEntity;
            foodtruckEntity.addAll(closedFoodtruckEntity);
            for (int i = 0; i < foodtruckEntity.size(); i++) {
                foodtruckEntity.get(i).setId(null);
                foodtruckEntity.get(i).setPassword(null);
                foodtruckEntity.get(i).setRole(null);
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        model.addAttribute("isUser", role.contains("ROLE_USER"));
        model.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            model.addAttribute("isUserLogged", false);
        else
            model.addAttribute("isUserLogged", true);

        model.addAttribute("user", u);
        model.addAttribute("foodtrucks", foodtruckEntity);

        model.addAttribute("isUdActive", "lg:bg-[#1E0342] lg:text-white");
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

    @RequestMapping("/saveFeedback")
    public String saveFeedback(FeedbackModel feedbackModel, RedirectAttributes m,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            UserEntity userEntity = userServiceImpl.findUser(customUserDetails.getUsername());
            FoodtruckEntity foodtruckEntity = foodTruckServiceImpl
                    .findFoodTruckByEmail(feedbackModel.getFoodtruckEmail());

            FoodtruckFeedbacksEntity foodtruckFeedbacks = new FoodtruckFeedbacksEntity();
            foodtruckFeedbacks.setFeedback(feedbackModel.getFeedback());
            foodtruckFeedbacks.setRating(feedbackModel.getRating());
            foodtruckFeedbacks.setUserName(userEntity.getName());

            foodtruckEntity.getFeedbacks().add(foodtruckFeedbacks);
            foodTruckServiceImpl.updateFoodTruck(foodtruckEntity);

            m.addFlashAttribute("error", "Feedback Saved.");
            return "redirect:/user/foodtruckDetails/" + feedbackModel.getFoodtruckEmail();
        } catch (Exception e) {
            m.addFlashAttribute("error", "Something wrong.");
            return "redirect:/user/foodtruckDetails/" + feedbackModel.getFoodtruckEmail();
        }

    }

    @RequestMapping("/foodtruckDetails/{email}")
    public String showFoodTruckDetails(@PathVariable("email") String email, Model model) {
        FoodtruckEntity foodtruckEntity = foodTruckServiceImpl.findFoodTruckByEmail(email);

        foodtruckEntity.setId(null);
        foodtruckEntity.setPassword(null);
        foodtruckEntity.setRole(null);
        Set<String> categories = new HashSet<String>();
        for (MenuEntity menuEntity : foodtruckEntity.getMenuEntity())
            categories.add(menuEntity.getCategory());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        UserEntity userEntity = userServiceImpl.findUser(authentication.getName());

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        model.addAttribute("distance", decimalFormat.format(findDistance.calculateDistance(userEntity.getLat(),
                userEntity.getLongi(), foodtruckEntity.getLat(), foodtruckEntity.getLongi())));

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            model.addAttribute("isUserLogged", false);
        else
            model.addAttribute("isUserLogged", true);

        model.addAttribute("isUser", role.contains("ROLE_USER"));
        model.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));
        model.addAttribute("categories", categories);
        model.addAttribute("foodtruck", foodtruckEntity);
        return "foodtruckDetails";
    }

    @RequestMapping("/menuItemFilter/category={category}&email={email}")
    public String menuItemFilter(@PathVariable("category") String category, @PathVariable("email") String email,
            Model model) {
        FoodtruckEntity foodtruckEntity = foodTruckServiceImpl.findFoodTruckByEmail(email);
        Set<String> categories = new HashSet<String>();
        for (MenuEntity menuEntity : foodtruckEntity.getMenuEntity())
            categories.add(menuEntity.getCategory());

        List<MenuEntity> menuList = menuListServiceImpl.filterMenuList(category);

        foodtruckEntity.setMenuEntity(menuList);
        foodtruckEntity.setId(null);
        foodtruckEntity.setRole(null);
        foodtruckEntity.setPassword(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            model.addAttribute("isUserLogged", false);
        else
            model.addAttribute("isUserLogged", true);

        model.addAttribute("isUser", role.contains("ROLE_USER"));
        model.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));
        model.addAttribute("categories", categories);
        model.addAttribute("foodtruck", foodtruckEntity);

        return "foodtruckDetails";
    }

    @RequestMapping("/userProfile")
    public String userProfile(Authentication authentication, @ModelAttribute("error") String error, Model model) {
        UserEntity userEntity = userServiceImpl.findUser(authentication.getName());
        userEntity.setPassword(null);
        userEntity.setId(null);
        userEntity.setRole(null);
        model.addAttribute("user", userEntity);

        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            model.addAttribute("isUserLogged", false);
        else
            model.addAttribute("isUserLogged", true);

        model.addAttribute("isUser", role.contains("ROLE_USER"));
        model.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));
        model.addAttribute("error", error);
        model.addAttribute("isUpActive", "lg:bg-[#1E0342] lg:text-white");

        return "userProfile";
    }

    @RequestMapping("/updateUser")
    public String updateUserDetails(Authentication authentication, RedirectAttributes model, UserModel userModel) {
        try {
            UserEntity userEntity = userServiceImpl.findUser(authentication.getName());
            if (userModel.getName().length() >= 3) {
                userEntity.setName(userModel.getName());
            } else {
                throw new Exception();
            }
            userServiceImpl.updateUser(userEntity);
            model.addFlashAttribute("error", "Profile Updated Successfully.");
            return "redirect:/user/userProfile";
        } catch (Exception e) {
            model.addFlashAttribute("error", "Something wrong.");
            return "redirect:/user/userProfile";
        }
    }
}
