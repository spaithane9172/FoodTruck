package com.foodtruck.foodtruck.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodtruck.foodtruck.entity.ReviewUs;

@Repository
public interface ReviewUsRepo extends JpaRepository<ReviewUs, Long> {
    @Query("SELECT r FROM ReviewUs r ORDER BY id DESC LIMIT 5")
    public List<ReviewUs> getNewFiveReviews();

    @Query("SELECT r FROM ReviewUs r ORDER BY id DESC")
    public List<ReviewUs> findAll();
}
