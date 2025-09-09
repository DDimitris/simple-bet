package com.dimitris.microservices.simple_bet.records;

import com.dimitris.microservices.simple_bet.constraints.PositiveOdd;
import jakarta.validation.constraints.NotNull;

public record MatchOddsDto(
        @NotNull(message = "specifier is required")
        String specifier, // 1, 2, X System is followed
        @NotNull(message = "Odd is required")
        @PositiveOdd
        Double odd
) {
}
