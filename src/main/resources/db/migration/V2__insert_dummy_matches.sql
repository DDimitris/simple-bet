-- Insert 50 dummy matches with odds
DO $$
DECLARE
    i INT;
BEGIN
    FOR i IN 1..50 LOOP
        INSERT INTO matches (description, match_date, match_time, team_a, team_b, sport)
        VALUES (
            'Dummy match ' || i,
            CURRENT_DATE + (i || ' days')::INTERVAL,
            TIME '12:00:00' + (i || ' minutes')::INTERVAL,
            'TeamA_' || i,
            'TeamB_' || i,
            CASE WHEN i % 2 = 0 THEN 1 ELSE 2 END -- alternate sport: 1=FOOTBALL, 2=BASKETBALL
        )
        RETURNING id INTO STRICT i;

        -- Insert odds for the match (1, 2, X)
        INSERT INTO matches_odds (match_id, specifier, odd) VALUES (i, '1', 1.5 + (random() * 2));
        INSERT INTO matches_odds (match_id, specifier, odd) VALUES (i, '2', 1.5 + (random() * 2));
        INSERT INTO matches_odds (match_id, specifier, odd) VALUES (i, 'X', 1.5 + (random() * 2));
    END LOOP;
END $$;
