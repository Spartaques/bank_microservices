package com.andriibashuk.userauthservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final String[] WHILE_LIST_URLS = {
            "/register",
            "/login",
            "/users"
    };
    private final UserHeadersAuthenticationFilter userHeadersAuthenticationFilter;

    public WebSecurityConfig(UserHeadersAuthenticationFilter userHeadersAuthenticationFilter) {
        this.userHeadersAuthenticationFilter = userHeadersAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(userHeadersAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers(WHILE_LIST_URLS)
                .permitAll()
                .anyRequest()
                .authenticated();

        return httpSecurity.build();
    }
}
