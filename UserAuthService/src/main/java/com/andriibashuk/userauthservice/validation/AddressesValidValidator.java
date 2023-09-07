package com.andriibashuk.userauthservice.validation;

import com.andriibashuk.userauthservice.entity.Address;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class AddressesValidValidator implements ConstraintValidator<AddressesValid, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof List<?>) {
            List<?> valueList = (List<?>) value;
            if (!valueList.isEmpty() && valueList.get(0) instanceof Address) {
                ArrayList<Address> addressList = (ArrayList<Address>) value;
                return addressList.stream().allMatch(address -> address.getCity() != null && address.getState() != null && address.getStreet() != null);
            }
        }

        return false;
    }
}
