package com.bol.mancala.repository;

import com.bol.mancala.TestUtils;
import com.bol.mancala.dto.BoardDto;
import com.bol.mancala.model.BoardModel;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;
import com.bol.mancala.repository.entity.Board;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.repository.entity.GameStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJdbcTest
@Import({BeforeConvertGameEntityCallback.class, SaveGameEntityCallback.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;
    @MockBean
    SimpMessagingTemplate simpMessagingTemplate;

    @Test
    void shouldSaveGame() {
        Game game = new Game()
                .setStatus(GameStatus.NEW);
        Game saved = gameRepository.save(game);
        assertNotNull(saved.getId());
        assertEquals(saved.getStatus(), GameStatus.NEW);
    }

    @Test
    void shouldSendToWebsocketOnSave() {
        Game game = new Game()
                .setStatus(GameStatus.NEW)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        Game saved = gameRepository.save(game);
        assertNotNull(saved.getId());
        assertEquals(saved.getStatus(), GameStatus.NEW);

        Board board = Board.fromBoardModel(new BoardModel());
        BoardDto boardDto = BoardDto.fromBoard(board);
        Mockito.verify(simpMessagingTemplate).convertAndSend(SaveGameEntityCallback.TOPIC_GAME + saved.getId(), boardDto);
    }

    @Test
    void shouldSaveGameWithPlayers() {
        String player1Id = UUID.randomUUID().toString();
        String player2Id = UUID.randomUUID().toString();
        Game game = new Game()
                .setStatus(GameStatus.IN_PROGRESS)
                .setFirstPlayerId(player1Id)
                .setSecondPlayerId(player2Id)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        Game saved = gameRepository.save(game);
        assertNotNull(saved.getId());

        Game expected = new Game()
                .setId(saved.getId())
                .setStatus(GameStatus.IN_PROGRESS)
                .setFirstPlayerId(player1Id)
                .setSecondPlayerId(player2Id)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        assertEquals(expected, saved);
    }

    @Test
    void shouldUpdateGame() {
        String player1Id = UUID.randomUUID().toString();
        String player2Id = UUID.randomUUID().toString();
        Game game = new Game()
                .setStatus(GameStatus.NEW)
                .setFirstPlayerId(player1Id);
        Game saved = gameRepository.save(game);
        assertNotNull(saved.getId());

        saved.setStatus(GameStatus.IN_PROGRESS)
                .setSecondPlayerId(player2Id)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        Game actual = gameRepository.save(saved);

        Game expected = new Game()
                .setId(saved.getId())
                .setStatus(GameStatus.IN_PROGRESS)
                .setFirstPlayerId(player1Id)
                .setSecondPlayerId(player2Id)
                .setBoard(Board.fromBoardModel(new BoardModel()));
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateGameWithWinner() {
        String player1Id = UUID.randomUUID().toString();
        String player2Id = UUID.randomUUID().toString();
        Board board = TestUtils.createBoard(
                List.of(14, 5, 3, 2, 7, 0),
                4,
                List.of(4, 2, 5, 0, 8, 0),
                3,
                PlayerTurn.FIRST_PLAYER);
        Game game = new Game()
                .setStatus(GameStatus.NEW)
                .setFirstPlayerId(player1Id)
                .setSecondPlayerId(player2Id)
                .setBoard(board);
        Game saved = gameRepository.save(game);
        assertNotNull(saved.getId());

        saved.setStatus(GameStatus.FINISHED);
        saved.getBoard().setResult(WinnerType.SECOND_PLAYER);
        Game actual = gameRepository.save(saved);


        Board expectedBoard = TestUtils.createBoard(
                List.of(14, 5, 3, 2, 7, 0),
                4,
                List.of(4, 2, 5, 0, 8, 0),
                3,
                PlayerTurn.FIRST_PLAYER,
                WinnerType.SECOND_PLAYER);
        Game expected = new Game()
                .setId(saved.getId())
                .setStatus(GameStatus.FINISHED)
                .setFirstPlayerId(player1Id)
                .setSecondPlayerId(player2Id)
                .setBoard(expectedBoard);
        assertEquals(expected, actual);
    }
}
