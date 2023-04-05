package com.andriibashuk.clientauthservice.request;

import com.andriibashuk.clientauthservice.entity.Client;
import com.andriibashuk.clientauthservice.validation.ClientExistsInDatabaseByEmail;
import com.andriibashuk.clientauthservice.validation.PasswordMatches;
import com.andriibashuk.clientauthservice.validation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

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
    @ClientExistsInDatabaseByEmail
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
    private Client.Gender gender;
}
