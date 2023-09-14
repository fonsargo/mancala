package com.bol.mancala.service;

import com.bol.mancala.dto.BoardDto;
import com.bol.mancala.dto.GameDto;
import com.bol.mancala.model.BoardModel;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.repository.GameRepository;
import com.bol.mancala.repository.entity.Board;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.repository.entity.GameStatus;
import org.springframework.stereotype.Service;

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
        Game game = new Game()
                .setStatus(GameStatus.NEW)
                .setFirstPlayerId(playerId);
        game = gameRepository.save(game);
        return game.getId();
    }

    @Override
    public Optional<UUID> connectToGame(UUID gameId, String playerId) {
        return gameRepository.findById(gameId).map(game -> {
                    if (game.getStatus() != GameStatus.NEW) {
                        throw new IllegalArgumentException("Can't connect to already started game");
                    }
                    if (Objects.equals(game.getFirstPlayerId(), playerId)) {
                        throw new IllegalArgumentException("You are already connected to this game!");
                    }
                    game.setSecondPlayerId(playerId);
                    game.setStatus(GameStatus.IN_PROGRESS);
                    BoardModel boardModel = new BoardModel();
                    Board board = Board.fromBoardModel(boardModel);
                    game.setBoard(board);
                    gameRepository.save(game);
                    return gameId;
                }
        );
    }

    @Override
    public Optional<GameDto> loadGame(UUID gameId, String playerId) {
        return gameRepository.findById(gameId)
                .map(game -> {
                    if (!Objects.equals(game.getFirstPlayerId(), playerId) && !Objects.equals(game.getSecondPlayerId(), playerId)) {
                        throw new IllegalArgumentException("Player with ID: " + playerId + " doesn't participate in game with id: " + gameId);
                    }
                    boolean isFirstPlayer = Objects.equals(game.getFirstPlayerId(), playerId);
                    BoardDto board = game.getBoard() != null ? BoardDto.fromBoard(game.getBoard()) : null;
                    return new GameDto(board, isFirstPlayer, game.getStatus());
                });
    }

    @Override
    public void makeMove(UUID gameId, String playerId, int pitIndex) {
        //todo
        Game game = gameRepository.findById(gameId).orElseThrow();
        if (!game.getFirstPlayerId().equals(playerId) && !game.getSecondPlayerId().equals(playerId)) {
            throw new IllegalArgumentException("Player with ID: " + playerId + " doesn't participate in game with id: " + gameId);
        }

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Can't make move, game in status: " + game.getStatus());
        }

        Board board = game.getBoard();
        if (board == null) {
            throw new IllegalArgumentException("Game is not started yet!");
        }

        PlayerTurn playerTurn = game.getFirstPlayerId().equals(playerId) ? PlayerTurn.FIRST_PLAYER : PlayerTurn.SECOND_PLAYER;
        if (board.getPlayerTurn() != playerTurn) {
            throw new IllegalArgumentException("Can't make move, it's not your turn. Next player is: " + playerTurn);
        }

        BoardModel boardModel = BoardModel.fromBoard(board);
        boardModel.makeMove(pitIndex);
        board = Board.fromBoardModel(boardModel);
        game.setBoard(board);
        gameRepository.save(game);
    }
}
