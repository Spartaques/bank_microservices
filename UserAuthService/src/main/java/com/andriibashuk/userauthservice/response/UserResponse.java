package com.andriibashuk.userauthservice.response;

import com.andriibashuk.userauthservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
    private Long createdBy;
    private Long lastModifiedBy;
    private Short age;
    private User.Gender gender;
}
