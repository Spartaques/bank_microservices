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
import java.util.List;

@Component
@Log
public class UserHeadersAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("UserHeadersAuthenticationFilter ");
        if(request.getHeader("userId") == null) {
            filterChain.doFilter(request, response);
            return;
        }
        final Long userId = Long.parseLong(request.getHeader("userId"));
        final String email = request.getHeader("email");
        log.info("getAuthentication "+SecurityContextHolder.getContext().getAuthentication());
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = new User(userId, email);
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("APPLICATION_APPROVE"));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,"", authorityList);
            log.info("set auth to token "+auth);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
