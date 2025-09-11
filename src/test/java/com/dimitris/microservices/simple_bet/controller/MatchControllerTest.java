package com.dimitris.microservices.simple_bet.controller;

import com.dimitris.microservices.simple_bet.records.MatchRequestDto;
import com.dimitris.microservices.simple_bet.records.MatchResponseDto;
import com.dimitris.microservices.simple_bet.service.MatchService;
import com.dimitris.microservices.simple_bet.utils.DbResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.dimitris.microservices.simple_bet.utils.HelperMethods.createMatchResponseDto;
import static com.dimitris.microservices.simple_bet.utils.HelperMethods.mockRequestContent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
@Tag("unit")
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MatchService matchService;

    // GET /api/matches
    @Test
    void testGetMatches_success() throws Exception {
        MatchResponseDto match1 = createMatchResponseDto(1L, "OSFP", "PAO");
        MatchResponseDto match2 = createMatchResponseDto(2L, "AEK", "PAO");
        List<MatchResponseDto> matches = List.of(match1, match2);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "teamA"));
        Page<MatchResponseDto> matchPage = new PageImpl<>(matches, pageable, matches.size());

        when(matchService.getMatches(any(Pageable.class))).thenReturn(matchPage);

        mockMvc.perform(get("/api/matches")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sortBy", "teamA")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].teamA").value("OSFP"))
                .andExpect(jsonPath("$.content[1].teamA").value("AEK"));
    }

    // POST /api/matches
    @Test
    void testCreateMatch_success() throws Exception {
        MatchResponseDto response = createMatchResponseDto(1L, "OSFP", "PAO");

        when(matchService.saveMatch(any(MatchRequestDto.class)))
                .thenReturn(DbResponse.success(response));

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockRequestContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamA").value("OSFP"))
                .andExpect(jsonPath("$.teamB").value("PAO"));
    }

    @Test
    void testCreateMatch_conflict() throws Exception {
        when(matchService.saveMatch(any(MatchRequestDto.class)))
                .thenReturn(DbResponse.failure("Match already exists"));

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockRequestContent))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Match already exists"));
    }

    // PUT /api/matches/{id}
    @Test
    void testUpdateMatch_success() throws Exception {
        Long matchId = 1L;
        MatchResponseDto updated = createMatchResponseDto(matchId, "OSFP", "PAO");

        when(matchService.updateMatch(eq(matchId), any(MatchRequestDto.class)))
                .thenReturn(DbResponse.success(updated));

        mockMvc.perform(put("/api/matches/{id}", matchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockRequestContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamA").value("OSFP"))
                .andExpect(jsonPath("$.teamB").value("PAO"));
    }

    @Test
    void testUpdateMatch_notFound() throws Exception {
        Long matchId = 99L;

        when(matchService.updateMatch(eq(matchId), any(MatchRequestDto.class)))
                .thenReturn(DbResponse.failure("Match not found"));

        mockMvc.perform(put("/api/matches/{id}", matchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockRequestContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Match not found"));
    }

    // DELETE /api/matches/{id}
    @Test
    void testDeleteMatch_success() throws Exception {
        Long matchId = 1L;

        when(matchService.deleteMatch(matchId))
                .thenReturn(DbResponse.success(null));

        mockMvc.perform(delete("/api/matches/{id}", matchId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteMatch_notFound() throws Exception {
        Long matchId = 99L;

        when(matchService.deleteMatch(matchId))
                .thenReturn(DbResponse.failure("Match not found"));

        mockMvc.perform(delete("/api/matches/{id}", matchId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Match not found"));
    }
}
