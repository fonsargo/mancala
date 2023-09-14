package com.bol.mancala.service;


import com.bol.mancala.repository.entity.Game;

import java.util.Optional;
import java.util.UUID;

public interface GameService {

    UUID createGame(String playerId);

    Game connectToGame(UUID gameId, String playerId);

    Game loadGame(UUID gameId, String playerId);

    Optional<Game> makeMove(UUID gameId, String playerId, int pitIndex);
}
