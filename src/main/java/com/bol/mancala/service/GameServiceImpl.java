package com.bol.mancala.service;

import com.bol.mancala.dto.BoardDto;
import com.bol.mancala.dto.GameDto;
import com.bol.mancala.model.BoardModel;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.repository.GameRepository;
import com.bol.mancala.repository.entity.Board;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.repository.entity.GameStatus;
import com.bol.mancala.service.exception.ForbiddenException;
import com.bol.mancala.service.exception.GameLogicException;
import com.bol.mancala.service.exception.GameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
                .setFirstPlayerId(playerId)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        game = gameRepository.save(game);
        return game.getId();
    }

    @Override
    public void connectToGame(UUID gameId, String playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        if (game.getStatus() == GameStatus.NEW
                && !Objects.equals(game.getFirstPlayerId(), playerId)
                && game.getSecondPlayerId() == null) {
            game.setSecondPlayerId(playerId);
            game.setStatus(GameStatus.IN_PROGRESS);
            gameRepository.save(game);
        }
    }

    @Override
    public GameDto loadGame(UUID gameId, String playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        checkAccessRights(playerId, game);

        boolean isFirstPlayer = Objects.equals(game.getFirstPlayerId(), playerId);
        BoardDto board = BoardDto.fromBoard(game.getBoard());
        return new GameDto(board, isFirstPlayer, game.getStatus());
    }

    @Override
    public void makeMove(UUID gameId, String playerId, int pitIndex) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        checkAccessRights(playerId, game);

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new GameLogicException("Can't make move, game in status: " + game.getStatus());
        }

        Board board = game.getBoard();
        PlayerTurn playerTurn = game.getFirstPlayerId().equals(playerId) ? PlayerTurn.FIRST_PLAYER : PlayerTurn.SECOND_PLAYER;
        if (board.getPlayerTurn() != playerTurn) {
            throw new GameLogicException("Can't make move, it's not your turn. Next player is: " + board.getPlayerTurn());
        }

        BoardModel boardModel = BoardModel.fromBoard(board);
        boardModel.makeMove(pitIndex);
        board = Board.fromBoardModel(boardModel);
        game.setBoard(board);
        if (board.isGameOver()) {
            game.setStatus(GameStatus.FINISHED);
        }
        gameRepository.save(game);
    }

    private void checkAccessRights(String playerId, Game game) {
        if (!Objects.equals(game.getFirstPlayerId(), playerId) && !Objects.equals(game.getSecondPlayerId(), playerId)) {
            throw new ForbiddenException(playerId, game.getId());
        }
    }
}
