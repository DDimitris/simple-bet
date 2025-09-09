package com.dimitris.microservices.simple_bet.controller;

import com.dimitris.microservices.simple_bet.records.MatchRequestDto;
import com.dimitris.microservices.simple_bet.records.MatchResponseDto;
import com.dimitris.microservices.simple_bet.service.MatchService;
import com.dimitris.microservices.simple_bet.utils.DbResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<Page<MatchResponseDto>> getMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy));
        Page<MatchResponseDto> matches = matchService.getMatches(pageable);
        return ResponseEntity.ok(matches);
    }


    @PostMapping
    public ResponseEntity<?> createMatch(@Valid @RequestBody MatchRequestDto matchRequestDto) {
        log.info("Received request to save match: {}", matchRequestDto);

        DbResponse<MatchResponseDto> result = matchService.saveMatch(matchRequestDto);

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
        }

        String error = result.getError().toLowerCase();

        Map<String, String> errorMap = Collections.singletonMap("error", result.getError());

        HttpStatus status;
        if (error.contains("already exists")) {
            status = HttpStatus.CONFLICT;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(errorMap);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(
            @PathVariable Long id,
            @Valid @RequestBody MatchRequestDto matchRequestDto
    ) {
        log.info("Received request to update match with ID {}: {}", id, matchRequestDto);

        DbResponse<MatchResponseDto> result = matchService.updateMatch(id, matchRequestDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getData());
        }

        Map<String, String> errorMap = Collections.singletonMap("error", result.getError());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable Long id) {
        log.info("Received request to delete match with ID {}", id);

        DbResponse<Void> result = matchService.deleteMatch(id);

        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, String> errorMap = Collections.singletonMap("error", result.getError());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }

}
