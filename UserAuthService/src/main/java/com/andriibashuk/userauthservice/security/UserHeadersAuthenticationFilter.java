package com.andriibashuk.userauthservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Log
public class UserHeadersAuthenticationFilter extends OncePerRequestFilter {
    private static final String[] WHILE_LIST_URLS = {
            "/register",
            "/login",
    };
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("userId") == null) {
            filterChain.doFilter(request, response);
            return;
        }
        List<String> authorities = new ArrayList<>();
        if(request.getHeader("authorities") != null) {
            authorities = Stream.of(request.getHeader("authorities")
                            .split(",", -1))
                    .map(s -> s.replace("\"", ""))
                    .collect(Collectors.toList());
        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = User.builder()
                    .id(Long.valueOf(request.getHeader("userId")))
                    .email(request.getHeader("email"))
                    .age(request.getHeader("age") != null ? Short.valueOf(request.getHeader("age")) : null)
                    .firstName(request.getHeader("firstName") != null ? request.getHeader("firstName") : null)
                    .lastName(request.getHeader("lastName") != null ? request.getHeader("lastName") : null)
                    .createdDate(request.getHeader("createdDate") != null ? ZonedDateTime.parse(request.getHeader("createdDate").replace("\"", "")): null)
                    .lastModifiedDate(request.getHeader("lastModifiedDate") != null ? ZonedDateTime.parse(request.getHeader("lastModifiedDate").replace("\"", "")) : null)
                    .createdBy(request.getHeader("createdBy") != null ? Long.valueOf(request.getHeader("createdBy")) : null)
                    .lastModifiedBy(request.getHeader("lastModifiedBy") != null ? Long.valueOf(request.getHeader("lastModifiedBy")) : null)
                    .build();
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorities.forEach(s -> authorityList.add(new SimpleGrantedAuthority(s)));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,"", authorityList);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
