package com.dimitris.microservices.simple_bet.repository;

import com.dimitris.microservices.simple_bet.entities.Match;
import com.dimitris.microservices.simple_bet.enums.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>, JpaSpecificationExecutor<Match> {
    boolean existsByTeamAAndTeamBAndMatchDateAndSport(String teamA, String teamB, LocalDate matchDate,
                                                      Sport sport);
}
