package com.andriibashuk.gateway.filter;

import com.andriibashuk.gateway.utils.JWTUtil;
import com.andriibashuk.gateway.utils.RouterValidator;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.java.Log;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Log
@Component
public class JwtClientAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtClientAuthGatewayFilterFactory.Config> {

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
                    jwt = jwtUtil.validateToken(token);
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
                        .header("firstName", claims.get("firstName").toString())
                        .header("lastName", claims.get("lastName").toString())
                        .header("age", claims.get("age").toString())
                        .header("email", claims.get("email").toString())
                        .header("gender", claims.get("gender").toString())
                        .header("createdDate", claims.get("createdDate").toString())
                        .header("lastModifiedDate", claims.get("lastModifiedDate").toString());
            }
            return chain.filter(exchange);
        });
    }


    public static class Config {
        //Put the configuration properties for your filter here
    }
}
