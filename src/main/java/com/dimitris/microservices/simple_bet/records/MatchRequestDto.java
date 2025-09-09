package com.dimitris.microservices.simple_bet.records;

import com.dimitris.microservices.simple_bet.constraints.DifferentTeams;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@DifferentTeams
public record MatchRequestDto(
        @NotNull(message = "Team A is required")
        @NotEmpty(message = "Team A cannot be empty")
        String teamA,
        @NotNull(message = "Team B is required")
        @NotEmpty(message = "Team B cannot be empty")
        String teamB,
        @NotNull(message = "Date of match is required")
        @FutureOrPresent(message = "Match time must be in the present or future")
        LocalDate matchDate,
        @NotNull(message = "Time of match is required")
        LocalTime matchTime,
        @NotNull(message = "Sport is required")
        @NotEmpty(message = "Sport cannot be empty")
        String sport,
        String description,
        @Size(min = 3, max = 3)
        @NotNull(message = "Exactly three odds are required")
        @NotEmpty(message = "Odds cannot be empty")
        List<@Valid MatchOddsDto> odds
) {
}


