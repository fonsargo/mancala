package com.bol.mancala;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.BoardHalf;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;

import java.util.List;

public class TestUtils {

    public static Board createBoard(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                    List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                    PlayerTurn playerTurn, WinnerType result) {
        BoardHalf firstHalf = new BoardHalf(firstPlayerPits, firstPlayerLargePit);
        BoardHalf secondHalf = new BoardHalf(secondPlayerPits, secondPlayerLargePit);
        return new Board(firstHalf, secondHalf, playerTurn, result);
    }

    public static Board createBoard(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                    List<Integer> secondPlayerPits, int secondPlayerLargePit) {
        return new Board(firstPlayerPits, firstPlayerLargePit, secondPlayerPits, secondPlayerLargePit);
    }
}
