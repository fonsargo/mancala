package com.bol.mancala;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.BoardHalf;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.GameResult;

import java.util.List;

public class TestUtils {

    public static Board createBoard(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                    List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                    PlayerTurn playerTurn, GameResult winner) {
        BoardHalf firstBoard = new BoardHalf(firstPlayerPits, firstPlayerLargePit);
        BoardHalf secondBoard = new BoardHalf(secondPlayerPits, secondPlayerLargePit);
        return new Board(firstBoard, secondBoard, playerTurn, winner);
    }
}
