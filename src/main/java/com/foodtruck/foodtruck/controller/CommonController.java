package com.foodtruck.foodtruck.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foodtruck.foodtruck.entity.Blog;
import com.foodtruck.foodtruck.entity.ReviewUs;
import com.foodtruck.foodtruck.model.BlogModel;
import com.foodtruck.foodtruck.model.ContactModel;
import com.foodtruck.foodtruck.model.ReviewUsModel;
import com.foodtruck.foodtruck.service.BlogServiceImpl;
import com.foodtruck.foodtruck.service.ContactServiceImpl;
import com.foodtruck.foodtruck.service.FoodTruckServiceImpl;
import com.foodtruck.foodtruck.service.ReviewServiceImpl;
import com.foodtruck.foodtruck.service.UserServiceImpl;

@Controller
@RequestMapping("/common")
public class CommonController {
    @Autowired
    ReviewServiceImpl reviewServiceImpl;

    @Autowired
    ContactServiceImpl contactServiceImpl;

    @Autowired
    BlogServiceImpl blogServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    FoodTruckServiceImpl foodTruckServiceImpl;

    @RequestMapping("/reviewUs")
    public String reviewUsPage(Authentication authentication, Model model, @ModelAttribute("error") String error) {
        List<ReviewUs> reviewUs = reviewServiceImpl.getReviews();
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            model.addAttribute("isUserLogged", false);
        else
            model.addAttribute("isUserLogged", true);

        model.addAttribute("isUser", role.contains("ROLE_USER"));
        model.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));
        model.addAttribute("reviews", reviewUs);
        model.addAttribute("error", error);
        model.addAttribute("isRActive", "bg-[#1E0342] text-white");

        return "reviewUs";
    }

    @RequestMapping("/addNewReview")
    public String addNewReview(Authentication authentication, ReviewUsModel reviewUsModel, RedirectAttributes model) {
        if (reviewUsModel.getMsg().length() >= 3) {
            reviewServiceImpl.addNewReview(reviewUsModel);
            model.addFlashAttribute("error", "Review Posted Successfully.");
            return "redirect:/common/reviewUs";
        } else {
            model.addFlashAttribute("error", "Please Give Proper Review.");
            return "redirect:/common/reviewUs";
        }
    }

    @RequestMapping("/contact")
    public String contact(Authentication authentication, Model model, @ModelAttribute("error") String error) {
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            model.addAttribute("isUserLogged", false);
        else
            model.addAttribute("isUserLogged", true);

        model.addAttribute("isUser", role.contains("ROLE_USER"));
        model.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));
        model.addAttribute("error", error);
        model.addAttribute("isCActive", "bg-[#1E0342] text-white");

        return "contact";
    }

    @RequestMapping("/addContact")
    public String addContact(Authentication authentication, ContactModel contactModel, RedirectAttributes model) {
        if (contactModel.getMsg().length() >= 3) {
            contactServiceImpl.addContact(contactModel);
            model.addFlashAttribute("error", "Message Send Successfully.");
            return "redirect:/common/contact";
        } else {
            model.addFlashAttribute("error", "Please Send Proper Message.");
            return "redirect:/common/contact";
        }
    }

    @RequestMapping("/blogs")
    public String blogs(Authentication authentication, Model model, @ModelAttribute("error") String error) {
        List<Blog> blogs = blogServiceImpl.getAllBlogs();
        model.addAttribute("Blogs", blogs.isEmpty() ? null : blogs);
        model.addAttribute("myBlogs", blogServiceImpl.userBlogs(authentication.getName()).isEmpty() ? null
                : blogServiceImpl.userBlogs(authentication.getName()));

        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            model.addAttribute("isUserLogged", false);
        else
            model.addAttribute("isUserLogged", true);

        model.addAttribute("isUser", role.contains("ROLE_USER"));
        model.addAttribute("isFoodtruck", role.contains("ROLE_FOODTRUCK"));
        model.addAttribute("error", error);
        model.addAttribute("isBActive", "bg-[#1E0342] text-white");

        return "blogs";
    }

    @RequestMapping("/addBlog")
    public String addBlog(Authentication authentication, RedirectAttributes model, BlogModel blogModel,
            @RequestParam("photo") MultipartFile photo) throws IOException {
        if (blogModel.getMsg().length() >= 3 || !photo.isEmpty()) {
            Blog blog = new Blog();
            blog.setMsg(blogModel.getMsg());
            if (photo.isEmpty()) {
                blog.setImage(null);
            } else {
                blog.setImage(Base64.getEncoder().encodeToString(photo.getBytes()));
            }

            if (blogServiceImpl.saveNewBlog(blog) == null) {
                model.addFlashAttribute("error", "Something Wrong.");
                return "redirect:/common/blogs";
            } else {
                model.addFlashAttribute("error", "Blog Posted Successfully.");
                return "redirect:/common/blogs";
            }
        } else {
            model.addFlashAttribute("error", "Something Wrong.");
            return "redirect:/common/blogs";
        }
    }

    @RequestMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable("id") String id, RedirectAttributes model) {
        if (blogServiceImpl.deleteBlog(Long.valueOf(id))) {
            model.addAttribute("error", "Blog Deleted Successfully.");
            return "redirect:/common/blogs";
        } else {
            model.addAttribute("error", "Something Wrong.");
            return "redirect:/common/blogs";
        }
    }
}
