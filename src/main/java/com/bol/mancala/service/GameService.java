package com.bol.mancala.service;


import com.bol.mancala.dto.GameDto;

import java.util.UUID;

public interface GameService {

    UUID createGame(String playerId);

    void connectToGame(UUID gameId, String playerId);

    GameDto loadGame(UUID gameId, String playerId);

    void makeMove(UUID gameId, String playerId, int pitIndex);
}
