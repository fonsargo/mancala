package com.bol.mancala.service;


import com.bol.mancala.dto.GameDto;

import java.util.Optional;
import java.util.UUID;

public interface GameService {

    UUID createGame(String playerId);

    Optional<UUID> connectToGame(UUID gameId, String playerId);

    Optional<GameDto> loadGame(UUID gameId, String playerId);

    void makeMove(UUID gameId, String playerId, int pitIndex);
}
