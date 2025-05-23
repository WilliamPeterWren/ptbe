package com.tranxuanphong.userservice.validator;

public @interface PhoneConstraint {
    String message() default "invalid date of birth";

    int min() default 0;
}
