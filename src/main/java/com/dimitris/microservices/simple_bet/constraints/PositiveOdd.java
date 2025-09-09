package com.dimitris.microservices.simple_bet.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveOddValidator.class)
public @interface PositiveOdd {
    String message() default "Odds must be greater than 1.0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
