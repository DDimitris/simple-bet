-- Flyway migration script to create initial tables for matches and matches_odds

CREATE SCHEMA IF NOT EXISTS matches_service;

-- Create matches table
CREATE TABLE matches (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description TEXT DEFAULT NULL,
    match_date DATE NOT NULL,
    match_time TIME NOT NULL,
    team_a VARCHAR(100) NOT NULL,
    team_b VARCHAR(100) NOT NULL,
    sport INT NOT NULL
);

-- Create matches_odds table
CREATE TABLE matches_odds (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    match_id BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE, -- Foreign key to matches table
    specifier VARCHAR(50) NOT NULL,
    odd NUMERIC(5,2) NOT NULL
);
