package com.bol.mancala.service;

import com.bol.mancala.TestUtils;
import com.bol.mancala.dto.BoardDto;
import com.bol.mancala.dto.GameDto;
import com.bol.mancala.model.BoardModel;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;
import com.bol.mancala.repository.GameRepository;
import com.bol.mancala.repository.entity.Board;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.repository.entity.GameStatus;
import com.bol.mancala.service.exception.ForbiddenException;
import com.bol.mancala.service.exception.GameLogicException;
import com.bol.mancala.service.exception.GameNotFoundException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

public class GameServiceTest {

    GameRepository gameRepository = Mockito.mock(GameRepository.class);
    GameServiceImpl gameService = new GameServiceImpl(gameRepository);

    @Test
    void shouldCreateGame() {
        String playerId = UUID.randomUUID().toString();
        UUID gameId = UUID.randomUUID();

        Game expected = new Game()
                .setStatus(GameStatus.NEW)
                .setFirstPlayerId(playerId)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        Game stored = new Game()
                .setId(gameId)
                .setStatus(GameStatus.NEW)
                .setFirstPlayerId(playerId)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        Mockito.when(gameRepository.save(eq(expected))).thenReturn(stored);

        UUID uuid = gameService.createGame(playerId);
        Mockito.verify(gameRepository).save(eq(expected));
        assertEquals(gameId, uuid);
    }

    @Nested
    class ConnectToGame {

        @Test
        void shouldConnectToGame() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setStatus(GameStatus.NEW)
                    .setFirstPlayerId(player1Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            gameService.connectToGame(gameId, player2Id);

            Game expected = new Game()
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.verify(gameRepository).save(eq(expected));
        }

        @Test
        void shouldThrowGameNotFoundException() {
            UUID gameId = UUID.randomUUID();
            String playerId = UUID.randomUUID().toString();

            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.empty());

            assertThrows(GameNotFoundException.class, () -> gameService.connectToGame(gameId, playerId));
        }

        @Test
        void shouldNotConnectSamePlayer2Times() {
            UUID gameId = UUID.randomUUID();
            String playerId = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setStatus(GameStatus.NEW)
                    .setFirstPlayerId(playerId)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            gameService.connectToGame(gameId, playerId);

            Mockito.verify(gameRepository).findById(eq(gameId));
            Mockito.verifyNoMoreInteractions(gameRepository);
        }

        @Test
        void shouldNotConnectIfStatusNotNew() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            gameService.connectToGame(gameId, player2Id);

            Mockito.verify(gameRepository).findById(eq(gameId));
            Mockito.verifyNoMoreInteractions(gameRepository);
        }

        @Test
        void shouldNotConnectIfSecondPlayerExists() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            String player3Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setStatus(GameStatus.NEW)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            gameService.connectToGame(gameId, player3Id);

            Mockito.verify(gameRepository).findById(eq(gameId));
            Mockito.verifyNoMoreInteractions(gameRepository);
        }
    }

    @Nested
    class LoadGame {

        @Test
        void shouldLoadGame() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            BoardModel model = TestUtils.createBoardModel(
                    List.of(0, 7, 7, 7, 7, 7),
                    1,
                    List.of(6, 6, 6, 6, 6, 6),
                    0,
                    PlayerTurn.FIRST_PLAYER);
            Game storedGame = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(model));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            GameDto gameDto = gameService.loadGame(gameId, player1Id);

            Board board = TestUtils.createBoard(
                    List.of(0, 7, 7, 7, 7, 7),
                    1,
                    List.of(6, 6, 6, 6, 6, 6),
                    0,
                    PlayerTurn.FIRST_PLAYER);
            BoardDto boardDto = BoardDto.fromBoard(board);
            GameDto expected = new GameDto(boardDto, true, GameStatus.IN_PROGRESS);
            assertEquals(expected, gameDto);
        }


        @Test
        void shouldThrowGameNotFoundException() {
            UUID gameId = UUID.randomUUID();
            String playerId = UUID.randomUUID().toString();

            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.empty());

            assertThrows(GameNotFoundException.class, () -> gameService.loadGame(gameId, playerId));
        }

        @Test
        void shouldThrowForbiddenException() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            String player3Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            assertThrows(ForbiddenException.class, () -> gameService.loadGame(gameId, player3Id));
        }
    }

    @Nested
    class MakeMove {
        @Test
        void shouldMakeMove() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            gameService.makeMove(gameId, player1Id, 1);

            Board board = TestUtils.createBoard(
                    List.of(6, 0, 7, 7, 7, 7),
                    1,
                    List.of(7, 6, 6, 6, 6, 6),
                    0,
                    PlayerTurn.SECOND_PLAYER);
            Game expected = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(board);
            Mockito.verify(gameRepository).save(eq(expected));
        }

        @Test
        void shouldFinishGame() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            Board board = TestUtils.createBoard(
                    Lists.newArrayList(0, 0, 0, 0, 0, 8),
                    55,
                    Lists.newArrayList(7, 6, 6, 6, 6, 6),
                    30,
                    PlayerTurn.FIRST_PLAYER);
            Game storedGame = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(board);
            Mockito.when(gameRepository.findById(Mockito.eq(gameId))).thenReturn(Optional.of(storedGame));

            gameService.makeMove(gameId, player1Id, 5);

            Board expectedBoard = TestUtils.createBoard(
                    List.of(0, 0, 0, 0, 0, 0),
                    64,
                    List.of(0, 0, 0, 0, 0, 0),
                    66,
                    PlayerTurn.SECOND_PLAYER,
                    WinnerType.SECOND_PLAYER);
            Game expected = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.FINISHED)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(expectedBoard);
            Mockito.verify(gameRepository).save(eq(expected));
        }

        @Test
        void shouldThrowForbiddenException() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            String player3Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            assertThrows(ForbiddenException.class, () -> gameService.makeMove(gameId, player3Id, 5));
        }

        @Test
        void shouldThrowExceptionIfGameFinished() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.FINISHED)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            GameLogicException exception = assertThrows(GameLogicException.class,
                    () -> gameService.makeMove(gameId, player1Id, 0));
            assertEquals("Can't make move, game in status: " + GameStatus.FINISHED, exception.getMessage());
        }

        @Test
        void shouldThrowExceptionIfNotPlayerTurn() {
            UUID gameId = UUID.randomUUID();
            String player1Id = UUID.randomUUID().toString();
            String player2Id = UUID.randomUUID().toString();
            Game storedGame = new Game()
                    .setId(gameId)
                    .setStatus(GameStatus.IN_PROGRESS)
                    .setFirstPlayerId(player1Id)
                    .setSecondPlayerId(player2Id)
                    .setBoard(Board.fromBoardModel(new BoardModel()));
            Mockito.when(gameRepository.findById(eq(gameId))).thenReturn(Optional.of(storedGame));

            GameLogicException exception = assertThrows(GameLogicException.class,
                    () -> gameService.makeMove(gameId, player2Id, 0));
            assertEquals("Can't make move, it's not your turn. Next player is: " + PlayerTurn.FIRST_PLAYER, exception.getMessage());
        }
    }
}
