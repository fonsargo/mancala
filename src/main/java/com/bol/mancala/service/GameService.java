package com.bol.mancala.service;

import com.bol.mancala.model.Game;

import java.util.Optional;

public interface GameService {

    Game createGame(String playerId);

    Optional<Game> connectToGame(long gameId, String playerId);

    Optional<Game> makeMove(long gameId, String playerId, int pitIndex);
}
