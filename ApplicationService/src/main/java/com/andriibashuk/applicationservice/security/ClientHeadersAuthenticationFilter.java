package com.andriibashuk.applicationservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;

@Component
public class ClientHeadersAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("clientId") == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = Client.builder()
                    .id(Long.valueOf(request.getHeader("clientId")))
                    .email(request.getHeader("email"))
                    .age(request.getHeader("age") != null ? Short.valueOf(request.getHeader("age")) : null)
                    .firstName(request.getHeader("firstName") != null ? request.getHeader("firstName") : null)
                    .lastName(request.getHeader("lastName") != null ? request.getHeader("lastName") : null)
                    .phone(request.getHeader("phone"))
                    .createdDate(request.getHeader("createdDate") != null ? ZonedDateTime.parse(request.getHeader("createdDate").replace("\"", "")): null)
                    .lastModifiedDate(request.getHeader("lastModifiedDate") != null ? ZonedDateTime.parse(request.getHeader("lastModifiedDate").replace("\"", "")) : null)
                    .createdBy(request.getHeader("createdBy") != null ? Long.valueOf(request.getHeader("createdBy")) : null)
                    .lastModifiedBy(request.getHeader("lastModifiedBy") != null ? Long.valueOf(request.getHeader("lastModifiedBy")) : null)
                    .build();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,"");
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
