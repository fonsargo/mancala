package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

//
//    private Board board;
//
//
//    public Game(String firstPlayerId) {
//        this.status = GameStatus.NEW;
//        this.board = new Board();
//        this.firstPlayerId = firstPlayerId;
//    }
//
//    public void addSecondPlayer(String playerId) {
//        if (status != GameStatus.NEW) {
//            throw new IllegalArgumentException("Can't add player to game in status: " + status);
//        }
//
//        secondPlayerId = playerId;
//        status = GameStatus.IN_PROGRESS;
//    }

}
