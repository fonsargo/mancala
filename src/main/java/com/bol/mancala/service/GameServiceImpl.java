package com.bol.mancala.service;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.BoardHalf;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.repository.GameRepository;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.repository.entity.GameStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public UUID createGame(String playerId) {
        Board board = new Board();
        Game game = new Game(board.getFirstPlayerHalf().getPits(), board.getFirstPlayerHalf().getLargePit(),
                board.getSecondPlayerHalf().getPits(), board.getSecondPlayerHalf().getLargePit(), playerId);
        game = gameRepository.save(game);
        return game.getId();
    }

    @Override
    public Game connectToGame(UUID gameId, String playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (game.getStatus() != GameStatus.NEW) {
            throw new IllegalArgumentException("Can't connect to already started game");
        }
        game.setSecondPlayerId(playerId);
        game.setStatus(GameStatus.IN_PROGRESS);
        game = gameRepository.save(game);
        return game;
    }

    @Override
    public Game loadGame(UUID gameId, String playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!Objects.equals(game.getFirstPlayerId(), playerId) && !Objects.equals(game.getSecondPlayerId(), playerId)) {
            return null;
//            throw new IllegalArgumentException("Player with ID: " + playerId + " doesn't participate in game with id: " + gameId);
        }
        return game;
    }

    @Override
    public Optional<Game> makeMove(UUID gameId, String playerId, int pitIndex) {
        return gameRepository.findById(gameId)
                .map(game -> {
                    if (!game.getFirstPlayerId().equals(playerId) && !game.getSecondPlayerId().equals(playerId)) {
                        throw new IllegalArgumentException("Player with ID: " + playerId + " doesn't participate in game with id: " + gameId);
                    }

                    if (game.getStatus() != GameStatus.IN_PROGRESS) {
                        throw new IllegalArgumentException("Can't make move, game in status: " + game.getStatus());
                    }

                    PlayerTurn playerTurn = game.getFirstPlayerId().equals(playerId) ? PlayerTurn.FIRST_PLAYER : PlayerTurn.SECOND_PLAYER;
                    if (game.getPlayerTurn() != playerTurn) {
                        throw new IllegalArgumentException("Can't make move, it's not your turn. Next player is: " + playerTurn);
                    }

                    if (pitIndex >= BoardHalf.PITS_COUNT) {
                        throw new IllegalArgumentException("Wrong pit index: " + pitIndex + ", pit index should be from 0 to 5");
                    }

                    Board board = new Board(game.getFirstPlayerPits(), game.getFirstPlayerLargePit(), game.getSecondPlayerPits(), game.getSecondPlayerLargePit());
                    board.makeMove(pitIndex, playerTurn);

                    game.setFirstPlayerPits(board.getFirstPlayerHalf().getPits());
                    game.setFirstPlayerLargePit(board.getFirstPlayerHalf().getLargePit());
                    game.setSecondPlayerPits(board.getSecondPlayerHalf().getPits());
                    game.setSecondPlayerLargePit(board.getSecondPlayerHalf().getLargePit());
                    game.setPlayerTurn(board.getPlayerTurn());
                    if (board.isGameOver()) {
                        game.setResult(board.getResult());
                        game.setStatus(GameStatus.FINISHED);
                    }
                    game = gameRepository.save(game);
                    return game;
                });
    }
}
