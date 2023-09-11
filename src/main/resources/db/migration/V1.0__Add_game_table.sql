CREATE TABLE game
(
    id                  BIGSERIAL PRIMARY KEY,
    status              TEXT NOT NULL,
    first_player_id     TEXT,
    second_player_id    TEXT
);

CREATE TABLE board
(
    game                        BIGINT PRIMARY KEY REFERENCES game(id) ON DELETE CASCADE,
    first_player_large_pit      INT NOT NULL,
    first_player_pits           INT[] NOT NULL,
    second_player_large_pit     INT NOT NULL,
    second_player_pits          INT[] NOT NULL,
    player_turn                 TEXT NOT NULL,
    winner                      TEXT
);