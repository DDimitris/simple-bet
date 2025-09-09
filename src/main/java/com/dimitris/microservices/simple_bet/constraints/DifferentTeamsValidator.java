package com.dimitris.microservices.simple_bet.constraints;

import com.dimitris.microservices.simple_bet.records.MatchRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentTeamsValidator implements ConstraintValidator<DifferentTeams, MatchRequestDto> {

    @Override
    public boolean isValid(MatchRequestDto value, ConstraintValidatorContext context) {
        if (value == null) return true; // Null validation is handled within the record
        if (value.teamA() == null || value.teamB() == null) return true;

        return !value.teamA().equalsIgnoreCase(value.teamB());
    }
}
