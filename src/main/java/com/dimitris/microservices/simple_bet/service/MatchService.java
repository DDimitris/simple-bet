package com.dimitris.microservices.simple_bet.service;

import com.dimitris.microservices.simple_bet.entities.Match;
import com.dimitris.microservices.simple_bet.enums.Sport;
import com.dimitris.microservices.simple_bet.records.MatchRequestDto;
import com.dimitris.microservices.simple_bet.records.MatchResponseDto;
import com.dimitris.microservices.simple_bet.repository.MatchRepository;
import com.dimitris.microservices.simple_bet.utils.DbResponse;
import com.dimitris.microservices.simple_bet.utils.MatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public Page<MatchResponseDto> getMatches(Pageable pageable, String teamA, String teamB, String sport) {
        Specification<Match> matchSpecification = getMatchSpecification(teamA, teamB, sport);
        return matchRepository.findAll(matchSpecification, pageable)
                .map(MatchMapper::fromEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public DbResponse<MatchResponseDto> saveMatch(MatchRequestDto matchRequestDto) {
        log.info("Saving match: {}", matchRequestDto);
        boolean matchAlreadyExists = matchRepository.existsByTeamAAndTeamBAndMatchDateAndSport(matchRequestDto.teamA(), matchRequestDto.teamB(),
                matchRequestDto.matchDate(), Sport.valueOf(matchRequestDto.sport().toUpperCase()));
        if (matchAlreadyExists) {
            log.warn("Match: {}, already exists. Aborting persistence.", matchRequestDto);
            return DbResponse.failure("Match already exists");
        }
        var match = matchRepository.save(MatchMapper.toEntity(matchRequestDto));
        log.info("Match saved: {}", match);
        return DbResponse.success(MatchMapper.fromEntity(match));
    }

    @Transactional(rollbackFor = Exception.class)
    public DbResponse<MatchResponseDto> updateMatch(Long id, MatchRequestDto matchRequestDto) {
        log.info("Updating match with ID {}: {}", id, matchRequestDto);

        Optional<Match> oldMatch = matchRepository.findById(id);
        if (oldMatch.isEmpty()) {
            return DbResponse.failure("Match with ID " + id + " not found");
        }
        Match updatedMatch = MatchMapper.toEntity(matchRequestDto);
        updatedMatch.setId(oldMatch.get().getId());
        matchRepository.save(updatedMatch);

        log.info("Match with ID {} updated successfully", id);
        return DbResponse.success(MatchMapper.fromEntity(updatedMatch));
    }

    @Transactional(rollbackFor = Exception.class)
    public DbResponse<Void> deleteMatch(Long matchId) {
        log.info("Deleting match with id: {}", matchId);

        Optional<Match> match = matchRepository.findById(matchId);

        if (match.isEmpty()) {
            log.warn("Match with id {} doesn't exist", matchId);
            return DbResponse.failure("Match with id " + matchId + " doesn't exist.");
        }

        matchRepository.delete(match.get());

        log.info("Match with id: {}", matchId);

        return DbResponse.success(null);
    }

    private Specification<Match> getMatchSpecification(String teamA, String teamB, String sport) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (teamA != null && !teamA.isEmpty()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("teamA")), "%" + teamA.toLowerCase() + "%"));
            }

            if (teamB != null && !teamB.isEmpty()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("teamB")), "%" + teamB.toLowerCase() + "%"));
            }

            if (sport != null && !sport.isEmpty()) {
                predicates = criteriaBuilder.and(predicates,
                        criteriaBuilder.equal(root.get("sport"), Sport.toCode(sport.toUpperCase())));
            }

            return predicates;
        };
    }
}

