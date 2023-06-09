package com.andriibashuk.userauthservice.request;

import com.andriibashuk.userauthservice.entity.User;
import com.andriibashuk.userauthservice.validation.PasswordMatches;
import com.andriibashuk.userauthservice.validation.UserExistsInDatabaseByEmail;
import com.andriibashuk.userauthservice.validation.ValidEmail;
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
public class RegisterClientRequest {
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
    private User.Gender gender;
    @NotNull
    private List<Long> rolesIds;
}
