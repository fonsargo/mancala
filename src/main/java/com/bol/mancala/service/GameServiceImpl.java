package com.bol.mancala.service;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.GameResult;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.repository.GameRepository;
import com.bol.mancala.dto.GameDto;
import com.bol.mancala.repository.entity.GameStatus;
import com.bol.mancala.repository.entity.PlayerTurn;
import com.bol.mancala.repository.entity.WinnerType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameDto createGame(String playerId) {
        Board board = new Board();
        Game game = new Game(board.getMyPits(), board.getMyLargePit(), board.getOpponentPits(), board.getOpponentLargePit(), playerId);
        game = gameRepository.save(game);
        return new GameDto(game.getId(), game.getFirstPlayerPits(), game.getFirstPlayerLargePit(), game.getSecondPlayerPits(), game.getSecondPlayerLargePit(), true, null);
    }

    @Override
    public Optional<GameDto> connectToGame(long gameId, String playerId) {
        return gameRepository.findById(gameId)
                .map(game -> {
                    if (game.getStatus() != GameStatus.NEW) {
                        throw new IllegalArgumentException("Can't add player to game in status: " + game.getStatus());
                    }
                    game.setSecondPlayerId(playerId);
                    game.setStatus(GameStatus.IN_PROGRESS);
                    game = gameRepository.save(game);
                    return new GameDto(game.getId(), game.getSecondPlayerPits(), game.getSecondPlayerLargePit(), game.getFirstPlayerPits(), game.getFirstPlayerLargePit(), false, null);
                });
    }

    @Override
    public Optional<GameDto> makeMove(long gameId, String playerId, int pitIndex) {
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

                    if (pitIndex >= Board.PITS_COUNT) {
                        throw new IllegalArgumentException("Wrong pit index: " + pitIndex + ", pit index should be from 0 to 5");
                    }

                    Board board;
                    if (playerTurn == PlayerTurn.FIRST_PLAYER) {
                        board = new Board(game.getFirstPlayerPits(), game.getFirstPlayerLargePit(), game.getSecondPlayerPits(), game.getSecondPlayerLargePit());
                    } else {
                        board = new Board(game.getSecondPlayerPits(), game.getSecondPlayerLargePit(), game.getFirstPlayerPits(), game.getFirstPlayerLargePit());
                    }
                    board = board.makeMove(pitIndex);

                    game.setFirstPlayerPits(playerTurn == PlayerTurn.FIRST_PLAYER ? board.getMyPits() : board.getOpponentPits());
                    game.setFirstPlayerLargePit(playerTurn == PlayerTurn.FIRST_PLAYER ? board.getMyLargePit() : board.getOpponentLargePit());
                    game.setSecondPlayerPits(playerTurn == PlayerTurn.SECOND_PLAYER ? board.getMyPits() : board.getOpponentPits());
                    game.setSecondPlayerLargePit(playerTurn == PlayerTurn.SECOND_PLAYER ? board.getMyLargePit() : board.getOpponentLargePit());
                    game.setPlayerTurn(board.isRepeatTurn() ? playerTurn : playerTurn.toggle());

                    if (board.isGameOver()) {
                        game.setStatus(GameStatus.FINISHED);
                        WinnerType winner;
                        if (board.getResult() == GameResult.WIN) {
                            winner = WinnerType.fromTurn(playerTurn);
                        } else if (board.getResult() == GameResult.LOSE) {
                            winner = WinnerType.fromTurn(playerTurn.toggle());
                        } else {
                            winner = WinnerType.DRAW;
                        }
                        game.setWinner(winner);
                    }

                    game = gameRepository.save(game);
                    if (playerTurn == PlayerTurn.FIRST_PLAYER) {
                        return new GameDto(game.getId(), game.getFirstPlayerPits(), game.getFirstPlayerLargePit(), game.getSecondPlayerPits(), game.getSecondPlayerLargePit(), game.getPlayerTurn() == playerTurn, board.getResult());
                    } else {
                        return new GameDto(game.getId(), game.getSecondPlayerPits(), game.getSecondPlayerLargePit(), game.getFirstPlayerPits(), game.getFirstPlayerLargePit(), game.getPlayerTurn() == playerTurn, board.getResult());
                    }
                });
    }
}
