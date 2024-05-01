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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foodtruck.foodtruck.entity.Blog;
import com.foodtruck.foodtruck.entity.ReviewUs;
import com.foodtruck.foodtruck.service.BlogServiceImpl;
import com.foodtruck.foodtruck.service.ReviewServiceImpl;

@Controller
@RequestMapping("/public")
public class PublicController {
    static final String PATH = "redirect:/public/";

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

        return "index";
    }

    @RequestMapping("/registerFoodTruck")
    public String registerFoodTruck(@ModelAttribute("error") String error, Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            m.addAttribute("error", error);
            return "registerFoodTruck";
        } else
            return PATH;
    }

    @RequestMapping("/loginPage")
    public String loginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            return "loginPage";
        else
            return PATH;

    }

    @RequestMapping("/registerUser")
    public String registerUser(@ModelAttribute("error") String error, Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            m.addAttribute("error", error);
            return "registerUser";
        } else
            return PATH;
    }

    @RequestMapping("/aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }

    @RequestMapping("/howToStartFoodtruck")
    public String howToStartFoodtruck(Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        m.addAttribute("isUser", role.contains("ROLE_USER"));
        m.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            m.addAttribute("isUserLogged", false);
        else
            m.addAttribute("isUserLogged", true);
        return "howToStartFoodTruck";
    }
}
