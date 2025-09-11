package com.dimitris.microservices.simple_bet.utils;

import com.dimitris.microservices.simple_bet.entities.Match;
import com.dimitris.microservices.simple_bet.enums.Sport;
import com.dimitris.microservices.simple_bet.records.MatchOddsDto;
import com.dimitris.microservices.simple_bet.records.MatchRequestDto;
import com.dimitris.microservices.simple_bet.records.MatchResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HelperMethods {

    public static final String mockRequestContent = """
            {
              "description": "Final",
              "matchDate": "2025-10-15",
              "matchTime": "20:30",
              "teamA": "Olimpiakos",
              "teamB": "Panathinaikos",
              "sport": "football",
              "odds": [
                {
                  "specifier": "1",
                  "odd": 1.50
                },
                {
                  "specifier": "X",
                  "odd": 3.20
                },
                {
                  "specifier": "2",
                  "odd": 2.10
                }
              ]
            }
            """;

    public static MatchResponseDto createMatchResponseDto(Long id, String teamA, String teamB) {
        MatchOddsDto matchOdd1 = new MatchOddsDto("1", 1.0);
        MatchOddsDto matchOdd2 = new MatchOddsDto("2", 1.0);
        MatchOddsDto matchOddX = new MatchOddsDto("X", 1.0);
        List<MatchOddsDto> matchOdds = List.of(matchOdd1, matchOdd2, matchOddX);
        return new MatchResponseDto(id, teamA, teamB,
                LocalDate.parse("2025-11-20"), LocalTime.parse("12:12"),
                "Description", Sport.BASKETBALL, matchOdds);
    }

    public static MatchRequestDto createMatchRequestDto(String teamA, String teamB) {
        MatchOddsDto matchOdd1 = new MatchOddsDto("1", 1.0);
        MatchOddsDto matchOdd2 = new MatchOddsDto("2", 1.0);
        MatchOddsDto matchOddX = new MatchOddsDto("X", 1.0);
        List<MatchOddsDto> matchOdds = List.of(matchOdd1, matchOdd2, matchOddX);
        return new MatchRequestDto("OSFP", "PAO",
                LocalDate.parse("2025-11-20"), LocalTime.parse("12:12"),
                "Basketball", "Description", matchOdds);
    }

    public static Match createMatch(Long id, String teamA, String teamB) {
        Match m = new Match();
        m.setId(id);
        m.setTeamA(teamA);
        m.setTeamB(teamB);
        m.setSport(Sport.BASKETBALL);
        m.setMatchDate(LocalDate.parse("2025-11-20"));
        m.setMatchTime(LocalTime.parse("12:12"));
        return m;
    }
}
