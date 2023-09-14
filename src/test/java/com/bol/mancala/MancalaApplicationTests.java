package com.bol.mancala;

import com.bol.mancala.model.BoardModel;
import com.bol.mancala.repository.GameRepository;
import com.bol.mancala.repository.entity.Board;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.repository.entity.GameStatus;
import com.bol.mancala.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MancalaApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private GameService gameService;
	@Autowired
	private GameRepository gameRepository;

	@Test
	void shouldSaveGame() throws Exception {
		this.mockMvc.perform(post("/game/create"))
				.andExpect(status().isFound());
	}

	@Test
	void shouldConnectToGame() throws Exception {
		String player1Id = UUID.randomUUID().toString();
		String player2Id = UUID.randomUUID().toString();
		UUID gameId = gameService.createGame(player1Id);

		MockHttpSession session = new MockHttpSession(null, player2Id);
		this.mockMvc.perform(get("/game/" + gameId + "/connect").session(session))
				.andExpect(status().isFound());

		Optional<Game> gameOptional = gameRepository.findById(gameId);
		assertThat(gameOptional).isNotEmpty();

		Game expected = new Game()
				.setId(gameId)
				.setStatus(GameStatus.IN_PROGRESS)
				.setFirstPlayerId(player1Id)
				.setSecondPlayerId(player2Id)
				.setBoard(Board.fromBoardModel(new BoardModel()));

		assertThat(gameOptional.get()).isEqualTo(expected);
	}

	@Test
	void shouldLoadGame() throws Exception {
		String player1Id = UUID.randomUUID().toString();
		String player2Id = UUID.randomUUID().toString();
		Game game = new Game()
				.setStatus(GameStatus.IN_PROGRESS)
				.setFirstPlayerId(player1Id)
				.setSecondPlayerId(player2Id)
				.setBoard(Board.fromBoardModel(new BoardModel()));
		Game saved = gameRepository.save(game);
		UUID gameId = saved.getId();

		MockHttpSession session = new MockHttpSession(null, player1Id);
		this.mockMvc.perform(get("/game/" + gameId).session(session))
				.andExpect(status().isOk());
	}

	@Test
	void shouldMakeMove() throws Exception {
		String player1Id = UUID.randomUUID().toString();
		String player2Id = UUID.randomUUID().toString();
		Game game = new Game()
				.setStatus(GameStatus.IN_PROGRESS)
				.setFirstPlayerId(player1Id)
				.setSecondPlayerId(player2Id)
				.setBoard(Board.fromBoardModel(new BoardModel()));
		Game saved = gameRepository.save(game);
		UUID gameId = saved.getId();

		MockHttpSession session = new MockHttpSession(null, player1Id);
		this.mockMvc.perform(post("/game/" + gameId + "/makeMove")
						.session(session)
						.param("pitIndex", "5"))
				.andExpect(status().isOk());
	}

	@Test
	void shouldReturnBadRequestIfNotPlayerTurn() throws Exception {
		String player1Id = UUID.randomUUID().toString();
		String player2Id = UUID.randomUUID().toString();
		Game game = new Game()
				.setStatus(GameStatus.IN_PROGRESS)
				.setFirstPlayerId(player1Id)
				.setSecondPlayerId(player2Id)
				.setBoard(Board.fromBoardModel(new BoardModel()));
		Game saved = gameRepository.save(game);
		UUID gameId = saved.getId();

		MockHttpSession session = new MockHttpSession(null, player2Id);
		this.mockMvc.perform(post("/game/" + gameId + "/makeMove")
						.session(session)
						.param("pitIndex", "5"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Can't make move, it's not your turn. Next player is: FIRST_PLAYER")));
	}

	@Test
	void shouldReturnBadRequestIfIndexOutOfBound() throws Exception {
		String player1Id = UUID.randomUUID().toString();
		String player2Id = UUID.randomUUID().toString();
		Game game = new Game()
				.setStatus(GameStatus.IN_PROGRESS)
				.setFirstPlayerId(player1Id)
				.setSecondPlayerId(player2Id)
				.setBoard(Board.fromBoardModel(new BoardModel()));
		Game saved = gameRepository.save(game);
		UUID gameId = saved.getId();

		MockHttpSession session = new MockHttpSession(null, player1Id);
		this.mockMvc.perform(post("/game/" + gameId + "/makeMove")
						.session(session)
						.param("pitIndex", "50"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Wrong pit index: 50, pit index should be from 0 to 5")));
	}

	@Test
	void shouldReturnForbiddenIfNotParticipateInGame() throws Exception {
		String player1Id = UUID.randomUUID().toString();
		String player2Id = UUID.randomUUID().toString();
		String player3Id = UUID.randomUUID().toString();
		Game game = new Game()
				.setStatus(GameStatus.IN_PROGRESS)
				.setFirstPlayerId(player1Id)
				.setSecondPlayerId(player2Id)
				.setBoard(Board.fromBoardModel(new BoardModel()));
		Game saved = gameRepository.save(game);
		UUID gameId = saved.getId();

		MockHttpSession session = new MockHttpSession(null, player3Id);
		this.mockMvc.perform(post("/game/" + gameId + "/makeMove")
						.session(session)
						.param("pitIndex", "5"))
				.andExpect(status().isForbidden())
				.andExpect(content().string(containsString("Player with ID: " + player3Id + " doesn&#39;t participate in this game with id: " + gameId)));
	}

}
