package com.andriibashuk.tmservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
    private Long createdBy;
    private Long lastModifiedBy;
    private Short age;
}