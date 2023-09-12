package com.bol.mancala.repository.entity;

import com.bol.mancala.model.GameStatus;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.GameResult;
import org.springframework.data.annotation.Id;

public class GameEntity {
    @Id
    private Long id;
    private GameStatus status;
    private String firstPlayerId;
    private String secondPlayerId;
    private PlayerTurn playerTurn;
    private GameResult winner;
}
