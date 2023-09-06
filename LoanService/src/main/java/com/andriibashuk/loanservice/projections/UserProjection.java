package com.andriibashuk.loanservice.projections;

import lombok.Data;
import lombok.Value;

@Value
@Data
public class UserProjection {
    String firstName;
    String lastName;
}
