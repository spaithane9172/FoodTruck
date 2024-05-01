package com.foodtruck.foodtruck.service;

import java.util.List;

import com.foodtruck.foodtruck.entity.Blog;

public interface BlogService {
    public Blog saveNewBlog(Blog blog);

    public List<Blog> getAllBlogs();

    public List<Blog> newFiveBlogs();

    public Blog updateBlog(Blog blog);

    public boolean deleteBlog(Long id);

    public List<Blog> userBlogs(String email);
}
