package com.foodtruck.foodtruck.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Set<String> role = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (role.contains("ROLE_FOODTRUCK"))
            response.sendRedirect("/foodTruck/foodTruckDashboard");
        else
            response.sendRedirect("/user/userDashboard");
    }

}
