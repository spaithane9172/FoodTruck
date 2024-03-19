package com.foodtruck.foodtruck.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodtruckEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String foodTruckName;
    private String email;
    private String password;
    private String role = "ROLE_FOODTRUCK";

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_foodtruck_id", referencedColumnName = "id")
    private List<MenuEntity> menuEntity;
}