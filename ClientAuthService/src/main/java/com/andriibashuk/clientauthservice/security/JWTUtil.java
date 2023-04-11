package com.andriibashuk.clientauthservice.security;

import com.andriibashuk.clientauthservice.entity.Client;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(Client client) throws IllegalArgumentException, JWTCreationException {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 24);
        Date expireDate = calendar.getTime();
        return JWT.create()
                .withSubject(String.valueOf(client.getId()))
                .withClaim("firstName", client.getFirstName())
                .withClaim("lastName", client.getLastName())
                .withClaim("age", Integer.valueOf(client.getAge()))
                .withClaim("email", client.getEmail())
                .withClaim("phone", client.getPhone())
                .withClaim("gender", String.valueOf(client.getGender()))
                .withClaim("createdDate", String.valueOf(client.getCreatedDate()))
                .withClaim("lastModifiedDate", String.valueOf(client.getLastModifiedDate()))
                .withIssuedAt(date)
                .withExpiresAt(expireDate)
                .withIssuer("Andrii Bashuk")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }

}
