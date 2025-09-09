package com.dimitris.microservices.simple_bet.utils;

import com.dimitris.microservices.simple_bet.entities.Match;
import com.dimitris.microservices.simple_bet.enums.Sport;
import com.dimitris.microservices.simple_bet.records.MatchOddsDto;
import com.dimitris.microservices.simple_bet.records.MatchRequestDto;
import com.dimitris.microservices.simple_bet.records.MatchResponseDto;

import java.util.List;

public class MatchMapper {

    public static MatchResponseDto fromEntity(Match entity) {
        List<MatchOddsDto> odds = entity.getOdds().stream().map(MatchOddsMapper::fromEntity).toList();
        return new MatchResponseDto(entity.getId(), entity.getTeamA(), entity.getTeamB(), entity.getMatchDate(), entity.getMatchTime(), entity.getDescription(), entity.getSport(), odds);
    }

    public static Match toEntity(MatchRequestDto dto) {
        Match match = new Match();
        match.setMatchDate(dto.matchDate());
        match.setMatchTime(dto.matchTime());
        match.setTeamA(dto.teamA());
        match.setTeamB(dto.teamB());
        match.setDescription(dto.description());
        match.setSport(Sport.valueOf(dto.sport().toUpperCase()));
        match.setOdds(dto.odds().stream().map(matchOddsDto -> MatchOddsMapper.toEntity(matchOddsDto, match)).toList());
        return match;
    }


}

