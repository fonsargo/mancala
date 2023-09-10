package com.bol.mancala.service;

import com.bol.mancala.model.Game;
import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.PlayerBoard;
import com.bol.mancala.repository.GameRepository;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GameServiceTest {

    GameRepository gameRepository = Mockito.mock(GameRepository.class);
    GameServiceImpl gameService = new GameServiceImpl(gameRepository);

    @Test
    void shouldCreateGame() {
        long playerId = 1L;
        Game expected = new Game()
                .setStatus(GameStatus.NEW)
                .setPlayerBoards(Map.of(
                        playerId,
                        new PlayerBoard(List.of(6, 6, 6, 6, 6, 6), 0)
                ));
        Game game = gameService.createGame(playerId);
        System.out.println(game);
        assertThat(game).isEqualTo(expected);
    }

    @Test
    void shouldConnectToGame() {
        long gameId = 1L;
        long player1Id = 1L;
        long player2Id = 2L;
        Game storedGame = new Game()
                .setId(gameId)
                .setStatus(GameStatus.NEW)
                .setPlayerBoards(Maps.newHashMap(
                        player1Id,
                        new PlayerBoard(List.of(6, 6, 6, 6, 6, 6), 0)
                ));
        Mockito.when(gameRepository.findById(Mockito.eq(gameId))).thenReturn(Optional.of(storedGame));

        Game expected = new Game()
                .setId(gameId)
                .setStatus(GameStatus.IN_PROGRESS)
                .setPlayerBoards(Map.of(
                        player1Id,
                        new PlayerBoard(List.of(6, 6, 6, 6, 6, 6), 0),
                        player2Id,
                        new PlayerBoard(List.of(6, 6, 6, 6, 6, 6), 0)
                ));
        Optional<Game> gameOptional = gameService.connectToGame(gameId, player2Id);
        assertThat(gameOptional).isNotEmpty();
        assertThat(gameOptional.get()).usingRecursiveComparison().ignoringFields("playerTurnId").isEqualTo(expected);
    }

    @Test
    void shouldMakeMove() {
        long gameId = 1L;
        long player1Id = 1L;
        long player2Id = 2L;
        int pitIndex = 0;

        Game expected = new Game()
                .setId(gameId)
                .setStatus(GameStatus.IN_PROGRESS)
                .setPlayerBoards(Map.of(
                        player1Id,
                        new PlayerBoard(List.of(0, 7, 7, 7, 7, 7), 1),
                        player2Id,
                        new PlayerBoard(List.of(6, 6, 6, 6, 6, 6), 0)
                ))
                .setPlayerTurnId(player1Id);
        Optional<Game> gameOptional = gameService.makeMove(gameId, player1Id, pitIndex);
        assertThat(gameOptional).isNotEmpty();
        assertThat(gameOptional.get()).isEqualTo(expected);
    }
}
