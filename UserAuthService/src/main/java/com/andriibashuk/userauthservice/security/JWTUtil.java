package com.andriibashuk.userauthservice.security;

import com.andriibashuk.userauthservice.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(User user, Set<String> privileges) throws IllegalArgumentException, JWTCreationException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 24);
        Date expireDate = calendar.getTime();
        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withClaim("firstName", user.getFirstName())
                .withClaim("lastName", user.getLastName())
                .withClaim("age", Integer.valueOf(user.getAge()))
                .withClaim("email", user.getEmail())
                .withClaim("gender", String.valueOf(user.getGender()))
                .withClaim("createdDate", String.valueOf(user.getCreatedDate()))
                .withClaim("lastModifiedDate", String.valueOf(user.getLastModifiedDate()))
                .withClaim("authorities", privileges.stream().collect(Collectors.joining(", ")))
                .withIssuedAt(date)
                .withExpiresAt(expireDate)
                .withIssuer("Andrii Bashuk")
                .sign(Algorithm.HMAC256(secret));
    }

}
