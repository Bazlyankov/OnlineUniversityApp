package com.studentsystemapp.interceptor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserActivityLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("User " + getUsername() + " accessing " + request.getRequestURI() + " at " + System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println("Response status: " + response.getStatus());
    }

    private String getUsername() {
        // Logic to retrieve the username from the security context or session
        SecurityContextHolder.getContext().getAuthentication().getName();
        return "Anonymous"; // Placeholder if username retrieval fails
    }
}

