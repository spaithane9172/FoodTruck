package com.foodtruck.foodtruck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodtruck.foodtruck.entity.MenuEntity;
import com.foodtruck.foodtruck.repo.MenuRepo;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepo menuRepo;

    @Override
    public MenuEntity addMenu(MenuEntity menuEntity) {
        return menuRepo.save(menuEntity);
    }

    @Override
    public Boolean deleteMenu(Long id) {
        menuRepo.deleteById(id);
        Optional<MenuEntity> menuEntity = menuRepo.findById(id);

        return menuEntity.isEmpty();

    }

    @Override
    public List<MenuEntity> getMenu(Long id) {
        return null;
    }

}
