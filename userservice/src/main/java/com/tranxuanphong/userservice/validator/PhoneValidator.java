package com.tranxuanphong.userservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class PhoneValidator implements ConstraintValidator<PhoneConstraint, String> {
    private int min;

    @Override
    public void initialize(PhoneConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(Objects.isNull(value)){
            return false;
        }

        if(!value.startsWith("0")){
            return false;
        }

        return value.length() == 10;
    }
}
