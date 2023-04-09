package com.andriibashuk.applicationservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Log
public class UserHeadersAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("userId") == null) {
            filterChain.doFilter(request, response);
            return;
        }
        final Long userId = Long.parseLong(request.getHeader("userId"));
        final String email = request.getHeader("email");
        List<String> authorities = new ArrayList<>();
        if(request.getHeader("authorities") != null) {
            authorities = Stream.of(request.getHeader("authorities")
                    .split(",", -1))
                    .map(s -> s.replace("\"", ""))
                    .collect(Collectors.toList());
        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = new User(userId, email);
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorities.forEach(s -> authorityList.add(new SimpleGrantedAuthority(s)));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,"", authorityList);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
