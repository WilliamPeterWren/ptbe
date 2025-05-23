package com.tranxuanphong.userservice.validator;

import jakarta.validation.Constraint; 
import jakarta.validation.Payload; 
 
import java.lang.annotation.Retention; 
import java.lang.annotation.Target; 
 
import static java.lang.annotation.ElementType.*; 
import static java.lang.annotation.RetentionPolicy.RUNTIME; 
@Target({ FIELD }) 
@Retention(RUNTIME) 
@Constraint(validatedBy = { DobValidator.class }) 
public @interface DobConstraint { 
    String message() default "invalid date of birth"; 
 
    int min() default 0; 
 
    Class<?>[] groups() default { }; 
    Class<? extends Payload>[] payload() default { }; 
} 
