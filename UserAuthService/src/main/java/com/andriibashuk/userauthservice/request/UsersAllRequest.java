package com.andriibashuk.userauthservice.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersAllRequest {
    @Digits(integer = 10, fraction = 0, message = "Value must be an integer with at most 10 digits.")
    private int id;
}
