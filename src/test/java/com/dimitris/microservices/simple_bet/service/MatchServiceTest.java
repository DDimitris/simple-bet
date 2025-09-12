package com.dimitris.microservices.simple_bet.service;

import com.dimitris.microservices.simple_bet.entities.Match;
import com.dimitris.microservices.simple_bet.enums.Sport;
import com.dimitris.microservices.simple_bet.records.MatchRequestDto;
import com.dimitris.microservices.simple_bet.records.MatchResponseDto;
import com.dimitris.microservices.simple_bet.repository.MatchRepository;
import com.dimitris.microservices.simple_bet.utils.DbResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.dimitris.microservices.simple_bet.utils.HelperMethods.createMatch;
import static com.dimitris.microservices.simple_bet.utils.HelperMethods.createMatchRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Tag("unit")
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    MatchServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMatch_success() {
        Match match = createMatch(1L, "OSFP", "PAO");
        MatchRequestDto matchRequestDto = createMatchRequestDto("OSFP", "PAO");
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        MatchResponseDto data = matchService.saveMatch(matchRequestDto).getData();

        assertThat(data.teamA()).isEqualTo("OSFP");
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void testGetPaginatedMatches_success() {
        int page = 0;
        int size = 2;
        String sortBy = "teamA";
        String direction = "ASC";

        Match match1 = createMatch(1L, "OSFP", "PAO");
        Match match2 = createMatch(2L, "AEK", "PAO");

        List<Match> matches = List.of(match1, match2);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Match> matchPage = new PageImpl<>(matches, pageable, matches.size());

        when(matchRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(matchPage);

        Page<MatchResponseDto> matches1 = matchService.getMatches(pageable, null, null, null);

        assertThat(!matches1.isEmpty()).isTrue();
        assertThat(matches1.getSize()).isEqualTo(2);

    }

    @Test
    void testUpdateMatch_success() {
        Long matchId = 1L;

        MatchRequestDto updateRequest = createMatchRequestDto("OSFP", "PAO");

        Match existingMatch = new Match();
        existingMatch.setId(matchId);
        existingMatch.setTeamA("PAOK");
        existingMatch.setTeamB("AEK");
        existingMatch.setMatchDate(LocalDate.of(2025, 12, 12));
        existingMatch.setMatchTime(LocalTime.of(12, 0));
        existingMatch.setSport(Sport.FOOTBALL);


        when(matchRepository.findById(matchId)).thenReturn(Optional.of(existingMatch));
        when(matchRepository.save(existingMatch)).thenReturn(existingMatch);

        DbResponse<MatchResponseDto> matchResponseDtoDbResponse = matchService.updateMatch(matchId, updateRequest);

        assertThat(matchResponseDtoDbResponse.isSuccess()).isTrue();
        assertThat(matchResponseDtoDbResponse.getData()).isNotNull();
        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(captor.capture());
        Match savedMatch = captor.getValue();

        assertThat(savedMatch.getId()).isEqualTo(matchId);
        assertThat(savedMatch.getTeamA()).isEqualTo(updateRequest.teamA());
        assertThat(savedMatch.getTeamB()).isEqualTo(updateRequest.teamB());
        assertThat(savedMatch.getSport()).isEqualTo(Sport.valueOf(updateRequest.sport().toUpperCase()));
    }

    @Test
    void testDeleteMatch_success() {
        Long matchId = 1L;
        Match match = new Match();
        match.setId(matchId);
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        doNothing().when(matchRepository).delete(match);

        DbResponse<Void> voidDbResponse = matchService.deleteMatch(matchId);

        assertThat(voidDbResponse.isSuccess()).isTrue();
        verify(matchRepository).delete(match);
    }

    @Test
    void testGetMatchesWithTeamFilter_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Match match = createMatch(1L, "TeamA", "TeamB");
        Page<Match> matchPage = new PageImpl<>(List.of(match));

        when(matchRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(matchPage);

        Page<MatchResponseDto> result = matchService.getMatches(pageable, "TeamA", null, Sport.BASKETBALL.name());

        assertEquals(1, result.getTotalElements());
        assertEquals("TeamA", result.getContent().getFirst().teamA());
    }

}
