package com.bol.mancala;

import com.bol.mancala.model.BoardModel;
import com.bol.mancala.model.BoardHalf;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;
import com.bol.mancala.repository.entity.Board;

import java.util.List;

public class TestUtils {

    public static BoardModel createBoardModel(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                              List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                              PlayerTurn playerTurn, WinnerType result) {
        BoardHalf firstHalf = new BoardHalf(firstPlayerPits, firstPlayerLargePit);
        BoardHalf secondHalf = new BoardHalf(secondPlayerPits, secondPlayerLargePit);
        return new BoardModel(firstHalf, secondHalf, playerTurn, result);
    }

    public static BoardModel createBoardModel(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                              List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                              PlayerTurn playerTurn) {
        BoardHalf firstHalf = new BoardHalf(firstPlayerPits, firstPlayerLargePit);
        BoardHalf secondHalf = new BoardHalf(secondPlayerPits, secondPlayerLargePit);
        return new BoardModel(firstHalf, secondHalf, playerTurn, null);
    }

    public static Board createBoard(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                    List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                    PlayerTurn playerTurn) {
        return Board.fromBoardModel(
                createBoardModel(firstPlayerPits, firstPlayerLargePit, secondPlayerPits, secondPlayerLargePit, playerTurn));
    }

    public static Board createBoard(List<Integer> firstPlayerPits, int firstPlayerLargePit,
                                    List<Integer> secondPlayerPits, int secondPlayerLargePit,
                                    PlayerTurn playerTurn, WinnerType winnerType) {
        return Board.fromBoardModel(
                createBoardModel(firstPlayerPits, firstPlayerLargePit, secondPlayerPits, secondPlayerLargePit, playerTurn, winnerType));
    }
}
