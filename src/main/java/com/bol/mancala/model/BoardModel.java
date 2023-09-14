package com.bol.mancala.model;

import com.bol.mancala.repository.entity.Board;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class BoardModel {

    public static final int PITS_COUNT = 6;
    public static final int INITIAL_STONES_COUNT = 6;

    private BoardHalf firstPlayerHalf;
    private BoardHalf secondPlayerHalf;
    private PlayerTurn playerTurn;
    private WinnerType result;

    public BoardModel() {
        this.firstPlayerHalf = new BoardHalf();
        this.secondPlayerHalf = new BoardHalf();
        this.playerTurn = PlayerTurn.FIRST_PLAYER;
    }

    public static BoardModel fromBoard(Board board) {
        BoardHalf firstPlayerHalf = new BoardHalf(board.getFirstPlayerPits(), board.getFirstPlayerLargePit());
        BoardHalf secondPlayerHalf = new BoardHalf(board.getSecondPlayerPits(), board.getSecondPlayerLargePit());
        return new BoardModel(firstPlayerHalf, secondPlayerHalf, board.getPlayerTurn(), board.getResult());
    }

    public void makeMove(int pitIndex) {
        if (pitIndex >= BoardModel.PITS_COUNT) {
            throw new IllegalArgumentException("Wrong pit index: " + pitIndex + ", pit index should be from 0 to 5");
        }

        boolean repeatTurn;
        if (playerTurn == PlayerTurn.FIRST_PLAYER) {
            repeatTurn = makeMove(pitIndex, firstPlayerHalf, secondPlayerHalf);
        } else {
            repeatTurn = makeMove(pitIndex, secondPlayerHalf, firstPlayerHalf);
        }
        if (!repeatTurn) {
            this.playerTurn = playerTurn.toggle();
        }
        checkFinished();
    }

    public boolean isGameOver() {
        return result != null;
    }

    /**
     * Make move from pit index in current player's half of board.
     *
     * @return true, if current player get extra turn, otherwise false
     */
    private boolean makeMove(int pitIndex, BoardHalf myHalf, BoardHalf opponentHalf) {
        Integer stones = myHalf.set(pitIndex, 0);
        if (stones == 0) {
            throw new IllegalArgumentException("Can't make move: pit with index: " + pitIndex + " is empty");
        }

        int startIndex = pitIndex + 1;
        while (stones > 0) {
            stones = myHalf.sowToMy(startIndex, stones, opponentHalf);
            startIndex = 0;

            if (stones > 0) {
                stones = myHalf.sowToLargePit(stones);
                if (stones == 0) {
                    // keep player turn
                    return true;
                }
            }

            stones = opponentHalf.sowToOpponent(stones);
        }

        return false;
    }

    private void checkFinished() {
        int firstPlayerHalfSum = firstPlayerHalf.getSum();
        int secondPlayerHalfSum = secondPlayerHalf.getSum();
        if (firstPlayerHalfSum == 0 || secondPlayerHalfSum == 0) {
            firstPlayerHalf.moveAllToLargePit();
            secondPlayerHalf.moveAllToLargePit();

            if (firstPlayerHalf.getLargePit() > secondPlayerHalf.getLargePit()) {
                this.result = WinnerType.FIRST_PLAYER;
            } else if (secondPlayerHalf.getLargePit() > firstPlayerHalf.getLargePit()) {
                this.result = WinnerType.SECOND_PLAYER;
            } else {
                this.result = WinnerType.DRAW;
            }
        }
    }
}
