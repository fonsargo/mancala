package com.bol.mancala.controller;

import com.bol.mancala.model.Board;
import com.bol.mancala.repository.entity.Player;
import com.bol.mancala.service.GameService;
import com.bol.mancala.dto.GameDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/game")
public class GameController {

    private static final String PLAYER_ATTRIBUTE = "player";

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/create")
    public ResponseEntity<GameDto> create(@RequestParam String name, HttpSession session) {
        Player player = new Player(session.getId(), name);
        session.setAttribute("player", player);
        GameDto game = gameService.createGame(player.getSessionId());
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/game/{gameId}")
                .buildAndExpand(game.getId()).toUri();
        return ResponseEntity.created(uri).body(game);
    }

    @PostMapping("/{gameId}/connect")
    public ResponseEntity<GameDto> connect(@PathVariable("gameId") long gameId, @RequestParam String name, HttpSession session) {
        Player player = new Player(session.getId(), name);
        session.setAttribute(PLAYER_ATTRIBUTE, player);
        Optional<GameDto> gameOptional = gameService.connectToGame(gameId, player.getSessionId());
        gameOptional.ifPresent(game -> messagingTemplate.convertAndSend("/topic/game/" + gameId, game));
        return ResponseEntity.of(gameOptional);
    }

    @PostMapping("/{gameId}/makeMove")
    public ResponseEntity<GameDto> makeMove(@PathVariable("gameId") long gameId, @RequestParam @Min(0) @Max(Board.PITS_COUNT - 1) Integer pitIndex, HttpSession session) {
        Player player = (Player) session.getAttribute(PLAYER_ATTRIBUTE);
        if (player == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<GameDto> gameOptional = gameService.makeMove(gameId, player.getSessionId(), pitIndex);
        gameOptional.ifPresent(game -> messagingTemplate.convertAndSend("/topic/game/" + gameId, game));
        return ResponseEntity.of(gameOptional);
    }

}
