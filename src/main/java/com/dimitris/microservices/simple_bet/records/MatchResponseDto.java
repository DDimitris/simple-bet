package com.dimitris.microservices.simple_bet.records;

import com.dimitris.microservices.simple_bet.enums.Sport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MatchResponseDto(
        Long id,
        String teamA,
        String teamB,
        LocalDate matchDate,
        LocalTime matchTime,
        String description,
        Sport sport,
        List<MatchOddsDto> odds
) {
}
