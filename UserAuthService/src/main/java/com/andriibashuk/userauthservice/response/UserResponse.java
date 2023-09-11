package com.andriibashuk.userauthservice.response;

import com.andriibashuk.userauthservice.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
    private Long createdBy;
    private Long lastModifiedBy;
    private Short age;
    private User.Gender gender;
    private Set<Address> addresses;
    private Set<Audit> audits;
    private Set<PhoneNumber> phoneNumbers;
}
