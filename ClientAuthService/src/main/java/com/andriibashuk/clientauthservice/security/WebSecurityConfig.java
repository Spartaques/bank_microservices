package com.andriibashuk.clientauthservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final String[] WHILE_LIST_URLS = {
            "/auth/client/register",
            "/auth/client/login"
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
