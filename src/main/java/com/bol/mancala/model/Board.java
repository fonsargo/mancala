package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Board {

    public static final int PITS_COUNT = 6;
    public static final int INITIAL_STONES_COUNT = 6;

    private List<Integer> firstPlayerPits;
    private Integer firstPlayerLargePit;
    private List<Integer> secondPlayerPits;
    private Integer secondPlayerLargePit;
    private PlayerTurn playerTurn;
    private WinnerType winner;

    public Board() {
        this.firstPlayerPits = IntStream.range(0, PITS_COUNT).map(operand -> INITIAL_STONES_COUNT).boxed().collect(Collectors.toList());
        this.firstPlayerLargePit = 0;
        this.secondPlayerPits = IntStream.range(0, PITS_COUNT).map(operand -> INITIAL_STONES_COUNT).boxed().collect(Collectors.toList());;
        this.secondPlayerLargePit = 0;
        this.playerTurn = PlayerTurn.FIRST_PLAYER;
    }

    public void makeMove(PlayerTurn player, int pitIndex) {
        if (player != playerTurn) {
            throw new IllegalArgumentException("Can't make move, it's not your turn. Next player is: " + playerTurn);
        }
        if (pitIndex >= PITS_COUNT) {
            //todo
            throw new IllegalArgumentException("Wrong pit index: " + pitIndex + ", pit index should be from 0 to 5");
        }

        if (playerTurn == PlayerTurn.FIRST_PLAYER) {
            makePlayerMove(pitIndex, firstPlayerPits, secondPlayerPits, this::addToFirstPlayerLargePit, PlayerTurn.SECOND_PLAYER);
        } else {
            makePlayerMove(pitIndex, secondPlayerPits, firstPlayerPits, this::addToSecondPlayerLargePit, PlayerTurn.FIRST_PLAYER);
        }

        checkFinished();
    }

    private void makePlayerMove(int pitIndex, List<Integer> myPits, List<Integer> oppositePits,
                                Consumer<Integer> myLargePitAdder, PlayerTurn nextPlayer) {
        Integer stones = myPits.set(pitIndex, 0);
        if (stones == 0) {
            //todo throw
            throw new IllegalArgumentException("Can't make move: pit with index: " + pitIndex + " is empty");
        }

        int startIndex = pitIndex + 1;
        while(stones > 0) {
            for (int i = startIndex; i < PITS_COUNT && stones > 0; i++) {
                //todo
                Integer currentStones = myPits.get(i);
                int oppositeIndex = PITS_COUNT - i - 1;
                Integer oppositeStones = oppositePits.get(oppositeIndex);
                if (currentStones == 0 && stones == 1 && oppositeStones > 0) {
                    myLargePitAdder.accept(stones + oppositeStones);
                    oppositePits.set(oppositeIndex, 0);
                } else {
                    myPits.set(i, currentStones + 1);
                }
                stones--;
            }

            if (stones > 0) {
                myLargePitAdder.accept(1);
                stones--;
                if (stones == 0) {
                    // keep player turn
                    return;
                }
            }

            for (int i = 0; i < PITS_COUNT && stones > 0; i++) {
                oppositePits.set(i, oppositePits.get(i) + 1);
                stones--;
            }

            startIndex = 0;
        }
        playerTurn = nextPlayer;
    }

    private void checkFinished() {
        int firstPlayerSum = firstPlayerPits.stream().mapToInt(Integer::intValue).sum();
        int secondPlayerSum = secondPlayerPits.stream().mapToInt(Integer::intValue).sum();
        if (firstPlayerSum == 0 || secondPlayerSum == 0) {
            if (firstPlayerSum > 0) {
                firstPlayerLargePit += firstPlayerSum;
                firstPlayerPits = IntStream.range(0, PITS_COUNT).map(operand -> 0).boxed().collect(Collectors.toList());
            }
            if (secondPlayerSum > 0) {
                secondPlayerLargePit += secondPlayerSum;
                secondPlayerPits = IntStream.range(0, PITS_COUNT).map(operand -> 0).boxed().collect(Collectors.toList());
            }

            if (firstPlayerLargePit > secondPlayerLargePit) {
                winner = WinnerType.FIRST_PLAYER;
            } else if (secondPlayerLargePit > firstPlayerLargePit) {
                winner = WinnerType.SECOND_PLAYER;
            } else {
                winner = WinnerType.DRAW;
            }
        }
    }

    private void addToFirstPlayerLargePit(int stones) {
        firstPlayerLargePit += stones;
    }

    private void addToSecondPlayerLargePit(int stones) {
        secondPlayerLargePit += stones;
    }
}
