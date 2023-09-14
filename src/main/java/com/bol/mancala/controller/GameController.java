package com.bol.mancala.controller;

import com.bol.mancala.dto.GameDto;
import com.bol.mancala.model.BoardHalf;
import com.bol.mancala.repository.entity.Game;
import com.bol.mancala.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/create")
    public String create(HttpSession session) {
        String sessionId = session.getId();
        UUID uuid = gameService.createGame(sessionId);
        return "redirect:/game/" + uuid;
    }

    @GetMapping("/{gameId}/connect")
    public String connect(@PathVariable("gameId") UUID gameId, HttpSession session) {
        String sessionId = session.getId();
        Game game = gameService.connectToGame(gameId, sessionId);
        GameDto gameDto = GameDto.fromGame(game);
        messagingTemplate.convertAndSend("/topic/game/" + gameId, gameDto);
        return "redirect:/game/" + gameId;
    }

    @GetMapping("/{gameId}")
    public String getGame(@PathVariable("gameId") UUID gameId, HttpSession session, Model model) {
        String sessionId = session.getId();
        Game game = gameService.loadGame(gameId, sessionId);
        if (game == null) {
            return "redirect:/game/" + gameId + "/connect";
        }
        GameDto gameDto = GameDto.fromGame(game);
        model.addAttribute("game", gameDto);
        model.addAttribute("isFirstPlayer", game.getFirstPlayerId().equals(sessionId));
        model.addAttribute("status", game.getStatus());
        return "game";
    }

    @PostMapping("/{gameId}/makeMove")
    @ResponseBody
    public ResponseEntity<GameDto> makeMove(@PathVariable("gameId") UUID gameId,
                                            @RequestParam @Min(0) @Max(BoardHalf.PITS_COUNT - 1) Integer pitIndex,
                                            HttpSession session) {
        Optional<GameDto> gameOptional = gameService.makeMove(gameId, session.getId(), pitIndex)
                .map(GameDto::fromGame);
        gameOptional.ifPresent(game -> messagingTemplate.convertAndSend("/topic/game/" + gameId, game));
        return ResponseEntity.of(gameOptional);
    }

}
