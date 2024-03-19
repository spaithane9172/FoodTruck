package com.foodtruck.foodtruck.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodtruck.foodtruck.entity.MenuEntity;

@Repository
public interface MenuRepo extends JpaRepository<MenuEntity, Long> {

}
