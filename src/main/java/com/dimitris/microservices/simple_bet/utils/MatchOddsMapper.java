package com.dimitris.microservices.simple_bet.utils;

import com.dimitris.microservices.simple_bet.entities.Match;
import com.dimitris.microservices.simple_bet.entities.MatchOdd;
import com.dimitris.microservices.simple_bet.records.MatchOddsDto;

public class MatchOddsMapper {
    public static MatchOddsDto fromEntity(MatchOdd entity) {
        return new MatchOddsDto(entity.getSpecifier(), entity.getOdd());
    }

    public static MatchOdd toEntity(MatchOddsDto dto, Match match) {
        MatchOdd matchOdd = new MatchOdd();
        matchOdd.setSpecifier(dto.specifier());
        matchOdd.setOdd(dto.odd());
        matchOdd.setMatch(match);
        return matchOdd;
    }
}
