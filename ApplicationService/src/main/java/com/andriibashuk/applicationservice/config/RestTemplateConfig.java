package com.andriibashuk.applicationservice.config;

import com.andriibashuk.applicationservice.http.ClientRestTemplate;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Base64;

@Configuration
@Log
public class RestTemplateConfig {
    @Value("${services.ClientAuthService.user}")
    private String user;
    @Value("${services.ClientAuthService.password}")
    private String password;
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean(name = "client")
    @LoadBalanced
    public RestTemplate clientRestTemplate() {
        String encoding = Base64.getEncoder().encodeToString((user + ":" + password).getBytes());
        return new RestTemplateBuilder().rootUri("http://ClientAuthService")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic "+encoding)
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000)).build();
    }
}
