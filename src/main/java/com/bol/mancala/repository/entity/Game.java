package com.bol.mancala.repository.entity;

import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    private UUID id;
    private GameStatus status;
    private List<Integer> firstPlayerPits;
    private int firstPlayerLargePit;
    private List<Integer> secondPlayerPits;
    private int secondPlayerLargePit;
    private String firstPlayerId;
    private String secondPlayerId;
    private PlayerTurn playerTurn;
    private WinnerType result;

    public Game(List<Integer> firstPlayerPits, int firstPlayerLargePit, List<Integer> secondPlayerPits, int secondPlayerLargePit, String firstPlayerId) {
        this.status = GameStatus.NEW;
        this.firstPlayerPits = firstPlayerPits;
        this.firstPlayerLargePit = firstPlayerLargePit;
        this.secondPlayerPits = secondPlayerPits;
        this.secondPlayerLargePit = secondPlayerLargePit;
        this.firstPlayerId = firstPlayerId;
        this.playerTurn = PlayerTurn.FIRST_PLAYER;
    }
}
