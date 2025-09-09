package com.dimitris.microservices.simple_bet.entities;

import com.dimitris.microservices.simple_bet.converter.SportOrCodeConverter;
import com.dimitris.microservices.simple_bet.enums.Sport;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "description")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    @Column(name = "match_time", nullable = false)
    private LocalTime matchTime;

    @Column(name = "team_a", nullable = false)
    private String teamA;

    @Column(name = "team_b", nullable = false)
    private String teamB;

    @Column(nullable = false)
    @Convert(converter = SportOrCodeConverter.class)
    private Sport sport;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MatchOdd> odds = new ArrayList<>();
}