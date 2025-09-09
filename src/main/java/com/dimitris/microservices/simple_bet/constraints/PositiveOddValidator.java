package com.dimitris.microservices.simple_bet.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PositiveOddValidator implements ConstraintValidator<PositiveOdd, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true; // Null validation is handled within the record
        return value >= 1.0;
    }
}
