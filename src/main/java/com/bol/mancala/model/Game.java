package com.bol.mancala.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Accessors(chain = true)
public class Game {

    private Long id;
    private GameStatus status = GameStatus.NEW;


    public Game addFirstPlayer(long playerId) {
        if (status != GameStatus.NEW) {
            throw new IllegalArgumentException("Can't add player to game in status: " + status);
        }

//        Board playerBoard = new Board(
//                ,
//                0
//        );
//        playerBoards.put(playerId, playerBoard);
//        if (playerBoards.size() == MAX_PLAYERS) {
//            status = GameStatus.IN_PROGRESS;
//            playerTurnId = playerBoards.keySet().stream().skip(random.nextInt(MAX_PLAYERS)).findFirst().orElse(playerId);
//        }
        return this;
    }

    public Game makeMove() {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Can't make move, game in status: " + status);
        }
        return this;
    }

}
