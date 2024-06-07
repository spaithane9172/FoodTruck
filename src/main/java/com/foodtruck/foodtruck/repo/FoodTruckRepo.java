package com.foodtruck.foodtruck.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodtruck.foodtruck.entity.FoodtruckEntity;

@Repository
public interface FoodTruckRepo extends JpaRepository<FoodtruckEntity, Long> {
    public FoodtruckEntity findByEmail(String email);

    @Query("SELECT f FROM FoodtruckEntity f WHERE f.status='Open'")
    public List<FoodtruckEntity> findOpenFoodTrucks();

    @Query("SELECT f FROM FoodtruckEntity f WHERE f.status='Closed'")
    public List<FoodtruckEntity> findClosedFoodTrucks();
}
