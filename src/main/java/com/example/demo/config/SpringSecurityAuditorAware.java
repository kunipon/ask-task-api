package com.example.demo.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.domain.model.DemoUserDetails;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return ((DemoUserDetails) authentication.getPrincipal()).getUsername();
    }
}