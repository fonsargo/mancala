package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Board {

    public static final int PITS_COUNT = 6;
    public static final int INITIAL_STONES_COUNT = 6;

    private List<Integer> myPits;
    private int myLargePit;
    private List<Integer> opponentPits;
    private int opponentLargePit;
    private boolean repeatTurn;
    private GameResult result;

    public Board() {
        this.myPits = IntStream.range(0, PITS_COUNT).map(operand -> INITIAL_STONES_COUNT).boxed().collect(Collectors.toList());
        this.myLargePit = 0;
        this.opponentPits = IntStream.range(0, PITS_COUNT).map(operand -> INITIAL_STONES_COUNT).boxed().collect(Collectors.toList());
        this.opponentLargePit = 0;
        this.repeatTurn = false;
    }

    public Board(List<Integer> myPits, int myLargePit, List<Integer> opponentPits, int opponentLargePit) {
        this.myPits = myPits;
        this.myLargePit = myLargePit;
        this.opponentPits = opponentPits;
        this.opponentLargePit = opponentLargePit;
    }

    public void makeMove(int pitIndex) {
        Integer stones = myPits.set(pitIndex, 0);
        if (stones == 0) {
            //todo throw
            throw new IllegalArgumentException("Can't make move: pit with index: " + pitIndex + " is empty");
        }

        boolean repeat = false;
        int startIndex = pitIndex + 1;
        while (stones > 0) {
            stones = sowToMy(startIndex, stones);
            startIndex = 0;

            if (stones > 0) {
                myLargePit += 1;
                stones--;
                if (stones == 0) {
                    // keep player turn
                    repeat = true;
                }
            }

            stones = sowToOpponent(stones);
        }

        repeatTurn = repeat;
        checkFinished();
    }

    public boolean isOver() {
        return result != null;
    }

    private Integer sowToMy(Integer index, Integer stones) {
        ListIterator<Integer> iterator = myPits.listIterator(index);
        ListIterator<Integer> oppositeIterator = opponentPits.listIterator(PITS_COUNT - index);
        while (stones > 0 && iterator.hasNext()) {
            Integer currentStones = iterator.next();
            Integer oppositeStones = oppositeIterator.previous();
            if (currentStones == 0 && stones == 1 && oppositeStones > 0) {
                myLargePit += stones + oppositeStones;
                oppositeIterator.set(0);
            } else {
                iterator.set(currentStones + 1);
            }
            stones--;
        }
        return stones;
    }

    private Integer sowToOpponent(Integer stones) {
        ListIterator<Integer> iterator = opponentPits.listIterator();
        while (stones > 0 && iterator.hasNext()) {
            Integer currentStones = iterator.next();
            iterator.set(currentStones + 1);
            stones--;
        }
        return stones;
    }

    private void checkFinished() {
        int mySum = getSum(myPits);
        int opponentSum = getSum(opponentPits);
        if (mySum == 0 || opponentSum == 0) {
            myLargePit += pickUpAll(myPits);
            opponentLargePit += pickUpAll(opponentPits);

            if (myLargePit > opponentLargePit) {
                result = GameResult.WIN;
            } else if (opponentLargePit > myLargePit) {
                result = GameResult.LOSE;
            } else {
                result = GameResult.DRAW;
            }
        }
    }

    private Integer getSum(List<Integer> pits) {
        return pits.stream().mapToInt(Integer::intValue).sum();
    }

    private int pickUpAll(List<Integer> pits) {
        return IntStream.range(0, pits.size()).map(i -> pits.set(i, 0)).sum();
    }
}
