CREATE TABLE game
(
    id                          UUID PRIMARY KEY,
    status                      TEXT NOT NULL,
    first_player_pits           INT[] NOT NULL,
    first_player_large_pit      INT NOT NULL,
    second_player_pits          INT[] NOT NULL,
    second_player_large_pit     INT NOT NULL,
    first_player_id             TEXT,
    second_player_id            TEXT,
    player_turn                 TEXT NOT NULL,
    result                      TEXT
);