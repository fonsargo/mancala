package com.bol.mancala.service;

import com.bol.mancala.model.Game;
import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.PlayerTurn;
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

//    public void makeMove(PlayerTurn player, int pitIndex) {
//        if (player != playerTurn) {
//            throw new IllegalArgumentException("Can't make move, it's not your turn. Next player is: " + playerTurn);
//        }
//        if (pitIndex >= PITS_COUNT) {
//            //todo
//            throw new IllegalArgumentException("Wrong pit index: " + pitIndex + ", pit index should be from 0 to 5");
//        }
//
//        if (playerTurn == PlayerTurn.FIRST_PLAYER) {
//            playerTurn = makePlayerMove(pitIndex, firstPlayerBoard, secondPlayerBoard, playerTurn);
//        } else {
//            playerTurn = makePlayerMove(pitIndex, secondPlayerBoard, firstPlayerBoard, playerTurn);
//        }
//
//        this.winner = checkFinished(firstPlayerBoard, secondPlayerBoard);
//    }
//
//    public void makeMove(String playerId, int pitIndex) {
//        if (status != GameStatus.IN_PROGRESS) {
//            throw new IllegalArgumentException("Can't make move, game in status: " + status);
//        }
//
//        if (!firstPlayerId.equals(playerId) && !secondPlayerId.equals(playerId)) {
//            throw new IllegalArgumentException("Player with ID: " + playerId + " doesn't participate in game with id: " + id);
//        }
//
//        PlayerTurn playerTurn = firstPlayerId.equals(playerId) ? PlayerTurn.FIRST_PLAYER : PlayerTurn.SECOND_PLAYER;
//
//        board.makeMove(playerTurn, pitIndex);
//        if (board.getWinner() != null) {
//            status = GameStatus.FINISHED;
//        }
//    }
}
