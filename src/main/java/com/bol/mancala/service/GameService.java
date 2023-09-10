package com.bol.mancala.service;

import com.bol.mancala.model.Game;

import java.util.Optional;

public interface GameService {

    Game createGame(long playerId);

    Optional<Game> connectToGame(long gameId, long playerId);

    Optional<Game> makeMove(long gameId, long playerId, int pitIndex);
}
