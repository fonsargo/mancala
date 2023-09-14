package com.bol.mancala;

import com.bol.mancala.model.BoardModel;
import com.bol.mancala.model.BoardHalf;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;

import java.util.List;

public class TestUtils {

    public static BoardModel createBoard(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                         List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                         PlayerTurn playerTurn, WinnerType result) {
        BoardHalf firstHalf = new BoardHalf(firstPlayerPits, firstPlayerLargePit);
        BoardHalf secondHalf = new BoardHalf(secondPlayerPits, secondPlayerLargePit);
        return new BoardModel(firstHalf, secondHalf, playerTurn, result);
    }

    public static BoardModel createBoard(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                         List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                         PlayerTurn playerTurn) {
        BoardHalf firstHalf = new BoardHalf(firstPlayerPits, firstPlayerLargePit);
        BoardHalf secondHalf = new BoardHalf(secondPlayerPits, secondPlayerLargePit);
        return new BoardModel(firstHalf, secondHalf, playerTurn, null);
    }
}
