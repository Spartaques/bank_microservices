package com.andriibashuk.userauthservice.request;

import com.andriibashuk.userauthservice.entity.*;
import com.andriibashuk.userauthservice.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(field = "password", fieldMatch = "matchingPassword")
public class RegisterUserRequest {
    @Length(min = 3, max = 75)
    @NotBlank
    @NotEmpty
    @NotNull
    private String firstName;
    @Length(min = 3, max = 75)
    @NotBlank
    @NotEmpty
    @NotNull
    private String lastName;
    @Length(min = 3, max = 150)
    @NotBlank
    @NotEmpty
    @NotNull
    @ValidEmail
    @UserExistsInDatabaseByEmail
    private String email;
    @Length(min = 5, max = 32)
    @NotBlank
    @NotEmpty
    @NotNull
    private String password;
    @Length(min = 5, max = 32)
    @NotBlank
    @NotEmpty
    @NotNull
    private String matchingPassword;
    @CreatedDate
    @Range(min = 18, max = 150)
    @NotNull
    @PositiveOrZero
    private Short age;
    @NotNull
    @GenderValid(array = {User.Gender.MALE, User.Gender.FEMALE})
    private String gender;
    @NotNull
    private Set<Long> rolesIds;
    @AddressesValid
    private Set<Address> addresses;
    @PhoneNumbersValid
    private Set<PhoneNumber> phoneNumbers;
    @AuditsValid
    private Set<Audit> audits;
    @CommentsValid
    private Set<UserComment> comments;
}
