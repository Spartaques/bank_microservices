package com.andriibashuk.applicationservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private final ClientHeadersAuthenticationFilter clientHeadersAuthenticationFilter;
    private final HeadersAuthenticationFilter headersAuthenticationFilter;
    private final UserHeadersAuthenticationFilter userHeadersAuthenticationFilter;

    public WebSecurityConfig(ClientHeadersAuthenticationFilter clientHeadersAuthenticationFilter, HeadersAuthenticationFilter headersAuthenticationFilter, UserHeadersAuthenticationFilter userHeadersAuthenticationFilter) {
        this.clientHeadersAuthenticationFilter = clientHeadersAuthenticationFilter;
        this.headersAuthenticationFilter = headersAuthenticationFilter;
        this.userHeadersAuthenticationFilter = userHeadersAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic()
                .disable()
                .csrf()
                .disable()
                .cors()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(headersAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(userHeadersAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(clientHeadersAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // create new session for each request, so memory will be cleaned
        return httpSecurity.build();
    }
}
