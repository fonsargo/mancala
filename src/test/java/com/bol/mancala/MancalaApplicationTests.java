package com.bol.mancala;

import com.bol.mancala.model.Game;
import com.bol.mancala.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class MancalaApplicationTests {

	@Autowired
	GameService gameService;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldSaveGame() {
		Game game = gameService.createGame("id1");
		System.out.println(game);
		Optional<Game> gameOptional = gameService.connectToGame(game.getId(), "id2");
		Game x = gameOptional.get();
		System.out.println(x);
	}

}
