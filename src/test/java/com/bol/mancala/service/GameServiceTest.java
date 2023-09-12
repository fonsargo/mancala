package com.bol.mancala.service;

import com.bol.mancala.TestUtils;
import com.bol.mancala.model.Board;
import com.bol.mancala.model.Game;
import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.GameResult;
import com.bol.mancala.repository.GameRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;

public class GameServiceTest {

    GameRepository gameRepository = Mockito.mock(GameRepository.class);
    GameServiceImpl gameService = new GameServiceImpl(gameRepository);

    @Test
    void shouldCreateGame() {
        String playerId = UUID.randomUUID().toString();

        Game expected = new Game(null, GameStatus.NEW, new Board(), playerId, null);
        gameService.createGame(playerId);
        Mockito.verify(gameRepository).save(eq(expected));
    }

    @Test
    void shouldConnectToGame() {
        long gameId = 1L;
        String player1Id = UUID.randomUUID().toString();
        String player2Id = UUID.randomUUID().toString();
        Game storedGame = new Game(gameId, GameStatus.NEW, new Board(), player1Id, null);
        Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

        gameService.connectToGame(gameId, player2Id);

        Game expected = new Game(gameId, GameStatus.IN_PROGRESS, new Board(), player1Id, player2Id);
        Mockito.verify(gameRepository).save(eq(expected));
    }

    @Test
    void shouldMakeMove() {
        long gameId = 1L;
        String player1Id = UUID.randomUUID().toString();
        String player2Id = UUID.randomUUID().toString();
        Game storedGame = new Game(gameId, GameStatus.IN_PROGRESS, new Board(), player1Id, player2Id);
        Mockito.when(gameRepository.findById(Mockito.eq(gameId))).thenReturn(Optional.of(storedGame));

        gameService.makeMove(gameId, player1Id, 1);

        Board expectedBoard = TestUtils.createBoard(
                List.of(6, 0, 7, 7, 7, 7),
                1,
                List.of(7, 6, 6, 6, 6, 6),
                0,
                PlayerTurn.SECOND_PLAYER,
                null);
        Game expected = new Game(gameId, GameStatus.IN_PROGRESS, expectedBoard, player1Id, player2Id);
        Mockito.verify(gameRepository).save(eq(expected));
    }

    @Test
    void shouldFinishGame() {
        long gameId = 1L;
        String player1Id = UUID.randomUUID().toString();
        String player2Id = UUID.randomUUID().toString();
        Board board = TestUtils.createBoard(
                Lists.newArrayList(0, 0, 0, 0, 0, 8),
                55,
                Lists.newArrayList(7, 6, 6, 6, 6, 6),
                30,
                PlayerTurn.FIRST_PLAYER,
                null);
        Game storedGame = new Game(gameId, GameStatus.IN_PROGRESS, board, player1Id, player2Id);
        Mockito.when(gameRepository.findById(Mockito.eq(gameId))).thenReturn(Optional.of(storedGame));

        gameService.makeMove(gameId, player1Id, 5);

        Board expectedBoard = TestUtils.createBoard(
                List.of(0, 0, 0, 0, 0, 0),
                64,
                List.of(0, 0, 0, 0, 0, 0),
                66,
                PlayerTurn.SECOND_PLAYER,
                GameResult.LOSE);
        Game expected = new Game(gameId, GameStatus.FINISHED, expectedBoard,player1Id, player2Id);
        Mockito.verify(gameRepository).save(eq(expected));
    }
}
