package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Board {

    private BoardHalf firstPlayerHalf;
    private BoardHalf secondPlayerHalf;
    private PlayerTurn playerTurn;
    private WinnerType result;

    public Board() {
        this.firstPlayerHalf = new BoardHalf();
        this.secondPlayerHalf = new BoardHalf();
    }

    public Board(List<Integer> firstPlayerPits, int firstPlayerLargePit, List<Integer> secondPlayerPits, int secondPlayerLargePit) {
        this.firstPlayerHalf = new BoardHalf(firstPlayerPits, firstPlayerLargePit);
        this.secondPlayerHalf = new BoardHalf(secondPlayerPits, secondPlayerLargePit);
    }

    public void makeMove(int pitIndex, PlayerTurn player) {
        boolean repeatTurn;
        if (player == PlayerTurn.FIRST_PLAYER) {
            repeatTurn = makeMove(pitIndex, firstPlayerHalf, secondPlayerHalf);
        } else {
            repeatTurn = makeMove(pitIndex, secondPlayerHalf, firstPlayerHalf);
        }
        this.playerTurn = repeatTurn ? player : player.toggle();
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
