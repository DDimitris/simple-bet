package com.dimitris.microservices.simple_bet.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE) //It will be applied at class level
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentTeamsValidator.class)
public @interface DifferentTeams {
    String message() default "Teams must be different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

