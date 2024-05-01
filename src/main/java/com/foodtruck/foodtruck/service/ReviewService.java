package com.foodtruck.foodtruck.service;

import java.util.List;

import com.foodtruck.foodtruck.entity.ReviewUs;
import com.foodtruck.foodtruck.model.ReviewUsModel;

public interface ReviewService {
    public ReviewUs addNewReview(ReviewUsModel reviewUsModel);

    public List<ReviewUs> getReviews();

    public List<ReviewUs> getNewFiveReviews();
}
