package com.foodtruck.foodtruck.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.foodtruck.foodtruck.entity.Blog;
import com.foodtruck.foodtruck.entity.FoodtruckEntity;
import com.foodtruck.foodtruck.entity.UserEntity;
import com.foodtruck.foodtruck.repo.BlogRepo;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogRepo blogRepo;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    FoodTruckServiceImpl foodTruckServiceImpl;

    @Override
    public Blog saveNewBlog(Blog blog) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (role.contains("ROLE_USER")) {
            UserEntity user = userServiceImpl.findUser(authentication.getName());
            blog.setName(user.getName());
            blog.setEmail(user.getEmail());
        } else {
            FoodtruckEntity foodtruckEntity = foodTruckServiceImpl.findFoodTruckByEmail(authentication.getName());
            blog.setName(foodtruckEntity.getName());
            blog.setEmail(foodtruckEntity.getEmail());
        }
        return blogRepo.save(blog);
    }

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepo.findAll();
    }

    @Override
    public List<Blog> newFiveBlogs() {
        return blogRepo.getNewFiveBlogs();
    }

    @Override
    public Blog updateBlog(Blog blog) {
        return blogRepo.save(blog);
    }

    @Override
    public boolean deleteBlog(Long id) {
        if (blogRepo.findById(id).isPresent()) {
            blogRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Blog> userBlogs(String email) {
        return blogRepo.findByEmail(email);
    }

}
