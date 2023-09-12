package com.bol.mancala.service;


import com.bol.mancala.dto.GameDto;

import java.util.Optional;

public interface GameService {

    GameDto createGame(String playerId);

    Optional<GameDto> connectToGame(long gameId, String playerId);

    Optional<GameDto> makeMove(long gameId, String playerId, int pitIndex);
}
