package com.bol.mancala.service;

import com.bol.mancala.model.Board;
import com.bol.mancala.model.BoardHalf;
import com.bol.mancala.model.GameResult;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.ListIterator;

@Component
public class GameEngine {

    public static final int PITS_COUNT = 6;
    public static final int INITIAL_STONES_COUNT = 6;

    /**
     * Make move from the pitIndex
     *
     * @param pitIndex index of player's pits
     * @param board board object
     * @return true, if there should be another player turn, or false, if current player gets extra turn
     */
    public boolean makeMoveAndChangeTurn(int pitIndex, Board board) {
        BoardHalf myHalf = board.getMyHalf();
        Integer stones = myHalf.set(pitIndex, 0);
        if (stones == 0) {
            //todo throw
            throw new IllegalArgumentException("Can't make move: pit with index: " + pitIndex + " is empty");
        }

        int startIndex = pitIndex + 1;
        while(stones > 0) {
            stones = sowToMy(startIndex, stones, board);
            startIndex = 0;

            if (stones > 0) {
                stones = myHalf.sowToLargePit(stones);
                if (stones == 0) {
                    // keep player turn
                    return false;
                }
            }

            stones = sowToOpponent(stones, board.getOpponentHalf());
        }
        return true;
    }

    @Nullable
    public GameResult checkFinished(Board board) {
        BoardHalf myHalf = board.getMyHalf();
        int mySum = myHalf.getSum();
        BoardHalf opponentHalf = board.getOpponentHalf();
        int opponentSum = opponentHalf.getSum();
        if (mySum == 0 || opponentSum == 0) {
            myHalf.moveAllToLargePit();
            opponentHalf.moveAllToLargePit();

            if (myHalf.getLargePit() > opponentHalf.getLargePit()) {
                return GameResult.WIN;
            } else if (opponentHalf.getLargePit() > myHalf.getLargePit()) {
                return GameResult.LOSE;
            } else {
                return GameResult.DRAW;
            }
        }
        return null;
    }

    private Integer sowToMy(Integer index, Integer stones, Board board) {
        BoardHalf myHalf = board.getMyHalf();
        ListIterator<Integer> iterator = myHalf.getPits().listIterator(index);
        while (stones > 0 && iterator.hasNext()) {
            Integer currentStones = iterator.next();
            if (currentStones == 0 && stones == 1) {
                captureOpponentStones(stones, board, PITS_COUNT - iterator.nextIndex());
            } else {
                iterator.set(currentStones + 1);
            }
            stones--;
        }
        return stones;
    }

    private void captureOpponentStones(Integer stones, Board board, int oppositeIndex) {
        BoardHalf opponentHalf = board.getOpponentHalf();
        Integer oppositeStones = opponentHalf.get(oppositeIndex);
        board.getMyHalf().addToLargePit(stones + oppositeStones);
        opponentHalf.set(oppositeIndex, 0);
    }

    private Integer sowToOpponent(Integer stones, BoardHalf opponentHalf) {
        ListIterator<Integer> iterator = opponentHalf.getPits().listIterator();
        while (stones > 0 && iterator.hasNext()) {
            Integer currentStones = iterator.next();
            iterator.set(currentStones + 1);
            stones--;
        }
        return stones;
    }
}
