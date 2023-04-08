package com.andriibashuk.userauthservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final String[] WHILE_LIST_URLS = {
            "/register",
            "/login"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(WHILE_LIST_URLS)
                .permitAll();

        return httpSecurity.build();
    }
}
