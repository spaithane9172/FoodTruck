package com.foodtruck.foodtruck.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodtruck.foodtruck.entity.Blog;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b ORDER BY id DESC LIMIT 5")
    public List<Blog> getNewFiveBlogs();

    @Query("SELECT b FROM Blog b ORDER BY id DESC")
    public List<Blog> findAll();

    @Query("SELECT b FROM Blog b WHERE b.email= ?1 ORDER BY id DESC")
    public List<Blog> findByEmail(String email);
}
