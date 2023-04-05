package com.andriibashuk.gateway.filter;

import com.andriibashuk.gateway.utils.JWTUtil;
import com.andriibashuk.gateway.utils.RouterValidator;
import lombok.extern.java.Log;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Log
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
                final String token = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
                try {
                    String subject = jwtUtil.validateTokenAndRetrieveSubject(token);
                } catch (Exception e) {
                    log.warning("something wrong with validation token " + token);
                }
            }
            return chain.filter(exchange);
        });
    }


    public static class Config {
        //Put the configuration properties for your filter here
    }
}
