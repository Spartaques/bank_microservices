package com.andriibashuk.clientauthservice.request;

import com.andriibashuk.clientauthservice.validation.ClientExistsInDatabaseByEmail;
import com.andriibashuk.clientauthservice.validation.ValidEmail;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Length(min = 3, max = 150)
    @NotBlank
    @NotEmpty
    @NotNull
    @ValidEmail
    private String email;
    @Length(min = 5, max = 32)
    @NotBlank
    @NotEmpty
    @NotNull
    private String password;
}
