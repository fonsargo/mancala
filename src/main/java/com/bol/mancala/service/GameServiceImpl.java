package com.bol.mancala.service;

import com.bol.mancala.model.Game;
import com.bol.mancala.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game createGame(String playerId) {
        Game game = new Game(playerId);
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> connectToGame(long gameId, String playerId) {
        return gameRepository.findById(gameId)
                .map(game -> {
                    game.addSecondPlayer(playerId);
                    return gameRepository.save(game);
                });
    }

    @Override
    public Optional<Game> makeMove(long gameId, String playerId, int pitIndex) {
        return gameRepository.findById(gameId)
                .map(game -> {
                    game.makeMove(playerId, pitIndex);
                    return gameRepository.save(game);
                });
    }
}
