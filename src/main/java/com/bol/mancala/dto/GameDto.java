package com.bol.mancala.dto;

import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;
import com.bol.mancala.repository.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private String id;
    private List<Integer> firstPlayerPits;
    private int firstPlayerLargePit;
    private List<Integer> secondPlayerPits;
    private int secondPlayerLargePit;
    private PlayerTurn playerTurn = PlayerTurn.FIRST_PLAYER;
    private WinnerType result;

    public static GameDto fromGame(Game game) {
        return new GameDto(game.getId().toString(), game.getFirstPlayerPits(), game.getFirstPlayerLargePit(),
                game.getSecondPlayerPits(), game.getSecondPlayerLargePit(), game.getPlayerTurn(), game.getResult());
    }
}
