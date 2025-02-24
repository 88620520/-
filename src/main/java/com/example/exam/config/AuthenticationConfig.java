package com.example.exam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class AuthenticationConfig {
    
    @Bean
    public AuthenticationManager authenticationManager(SecurityConfig securityConfig) throws Exception {
        return securityConfig.authenticationManagerBean();
    }
} 