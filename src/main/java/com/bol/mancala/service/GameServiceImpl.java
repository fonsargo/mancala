package com.bol.mancala.service;

import com.bol.mancala.model.Game;
import com.bol.mancala.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final Random random = new Random();

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game createGame(long playerId) {
        Game game = new Game();
//        game.addPlayer(playerId);
        gameRepository.save(game);
        return game;
    }

    @Override
    public Optional<Game> connectToGame(long gameId, long playerId) {
        return gameRepository.findById(gameId);
//                .map(game -> game.addPlayer(playerId));
    }

    @Override
    public Optional<Game> makeMove(long gameId, long playerId, int pitIndex) {
        return gameRepository.findById(gameId);
    }
}
