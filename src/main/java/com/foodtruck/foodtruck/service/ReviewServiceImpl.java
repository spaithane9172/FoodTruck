package com.foodtruck.foodtruck.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.foodtruck.foodtruck.entity.FoodtruckEntity;
import com.foodtruck.foodtruck.entity.ReviewUs;
import com.foodtruck.foodtruck.entity.UserEntity;
import com.foodtruck.foodtruck.model.ReviewUsModel;
import com.foodtruck.foodtruck.repo.ReviewUsRepo;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewUsRepo reviewUsRepo;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    FoodTruckServiceImpl foodTruckServiceImpl;

    @Override
    public ReviewUs addNewReview(ReviewUsModel reviewUsModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        ReviewUs reviewUs = new ReviewUs();

        if (role.contains("ROLE_USER")) {
            UserEntity user = userServiceImpl.findUser(authentication.getName());
            reviewUs.setName(user.getName());

        } else {
            FoodtruckEntity foodtruckEntity = foodTruckServiceImpl.findFoodTruckByEmail(authentication.getName());
            reviewUs.setName(foodtruckEntity.getName());
        }
        reviewUs.setMsg(reviewUsModel.getMsg());
        return reviewUsRepo.save(reviewUs);
    }

    @Override
    public List<ReviewUs> getReviews() {
        return reviewUsRepo.findAll();
    }

    @Override
    public List<ReviewUs> getNewFiveReviews() {
        return reviewUsRepo.getNewFiveReviews();
    }

}
