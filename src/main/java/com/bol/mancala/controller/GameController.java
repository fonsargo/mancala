package com.bol.mancala.controller;

import com.bol.mancala.dto.GameDto;
import com.bol.mancala.model.BoardModel;
import com.bol.mancala.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

@Controller
@RequestMapping("/game")
public class GameController {

    private static final String GAME_ID = "gameId";
    private static final String GAME = "game";

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
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
        gameService.connectToGame(gameId, sessionId);
        return "redirect:/game/" + gameId;
    }

    @GetMapping("/{gameId}")
    public String getGame(@PathVariable("gameId") UUID gameId, HttpSession session, Model model) {
        String sessionId = session.getId();
        GameDto gameDto = gameService.loadGame(gameId, sessionId);
        model.addAttribute(GAME_ID, gameId);
        model.addAttribute(GAME, gameDto);
        return "game";
    }

    @PostMapping("/{gameId}/makeMove")
    @ResponseBody
    public void makeMove(@PathVariable("gameId") UUID gameId,
                         @RequestParam @Valid @Min(0) @Max(BoardModel.PITS_COUNT - 1) Integer pitIndex,
                         HttpSession session) {
        gameService.makeMove(gameId, session.getId(), pitIndex);
    }

}
