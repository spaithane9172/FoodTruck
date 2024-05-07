package com.foodtruck.foodtruck.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foodtruck.foodtruck.entity.Blog;
import com.foodtruck.foodtruck.entity.ReviewUs;
import com.foodtruck.foodtruck.service.BlogServiceImpl;
import com.foodtruck.foodtruck.service.ReviewServiceImpl;

@Controller
public class HomeController {
    @Autowired
    ReviewServiceImpl reviewServiceImpl;

    @Autowired
    BlogServiceImpl blogServiceImpl;

    @RequestMapping("/")
    public String home(Model m) {
        List<ReviewUs> reviewUs = reviewServiceImpl.getNewFiveReviews();
        m.addAttribute("reviews", reviewUs);

        List<Blog> blogs = blogServiceImpl.newFiveBlogs();
        m.addAttribute("blogs", blogs);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        m.addAttribute("isUser", role.contains("ROLE_USER"));
        m.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            m.addAttribute("isUserLogged", false);
        else
            m.addAttribute("isUserLogged", true);
        m.addAttribute("isHActive", "lg:bg-[#1E0342] lg:text-white");

        return "index";
    }

}
