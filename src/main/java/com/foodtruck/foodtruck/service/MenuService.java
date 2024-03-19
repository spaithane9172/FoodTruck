package com.foodtruck.foodtruck.service;

import java.util.List;

import com.foodtruck.foodtruck.entity.MenuEntity;

public interface MenuService {
    public MenuEntity addMenu(MenuEntity menuEntity);

    public Boolean deleteMenu(Long id);

    public List<MenuEntity> getMenu(Long id);
}
