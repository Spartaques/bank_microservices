package com.andriibashuk.gateway.filter;

import com.andriibashuk.gateway.utils.JWTUtil;
import com.andriibashuk.gateway.utils.RouterValidator;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.java.Log;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Log
@Component
public class JwtClientAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtClientAuthGatewayFilterFactory.Config> {

    @Value("${client_jwt_secret}")
    private String clientJwtSecret;

    JWTUtil jwtUtil;
    RouterValidator routerValidator;

    public JwtClientAuthGatewayFilterFactory(JWTUtil jwtUtil, RouterValidator routerValidator) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.routerValidator = routerValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (routerValidator.isSecured.test(request)) {
                ServerHttpResponse response = exchange.getResponse();
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                final String authHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
                if(!authHeader.startsWith("Bearer ")) {
                    response.setStatusCode(HttpStatus.BAD_REQUEST);
                    return response.setComplete();
                }
                final String token = authHeader.substring(7);
                DecodedJWT jwt;
                try {
                    jwt = jwtUtil.validateToken(clientJwtSecret, token);
                } catch (Exception e) {
                    log.warning("something wrong with validation token " + token);
                    log.warning("exception " + e);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                if(jwt.getExpiresAt().before(new Date())) {
                    log.warning("token expired " + jwt.getExpiresAt());
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }

                Map<String, Claim> claims = jwt.getClaims();
                exchange.getRequest().mutate()
                        .header("clientId", jwt.getSubject())
                        .header("firstName", claims.containsKey("firstName") && !claims.get("firstName").isMissing() ? claims.get("firstName").toString() : null)
                        .header("lastName", claims.containsKey("lastName") && !claims.get("lastName").isMissing() ? claims.get("lastName").toString() : null)
                        .header("lastName", claims.containsKey("age") && !claims.get("age").isMissing() ? claims.get("age").toString() : null)
                        .header("email", claims.get("email").toString())
                        .header("phone", claims.get("phone").toString())
                        .header("gender", claims.containsKey("gender") && !claims.get("gender").isMissing() ? claims.get("gender").toString() : null)
                        .header("createdDate", claims.containsKey("createdDate") && !claims.get("createdDate").isMissing() ? claims.get("createdDate").toString() : null)
                        .header("lastModifiedDate", claims.containsKey("lastModifiedDate") && !claims.get("lastModifiedDate").isMissing() ? claims.get("lastModifiedDate").toString() : null);
            }
            return chain.filter(exchange);
        });
    }


    public static class Config {
        //Put the configuration properties for your filter here
    }
}
