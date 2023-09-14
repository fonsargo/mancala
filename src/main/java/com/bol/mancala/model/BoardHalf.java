package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BoardHalf {

    public static final int PITS_COUNT = 6;
    public static final int INITIAL_STONES_COUNT = 6;

    private List<Integer> pits;
    private int largePit;

    public BoardHalf() {
        this.pits = IntStream.range(0, PITS_COUNT).map(operand -> INITIAL_STONES_COUNT).boxed().collect(Collectors.toList());
        this.largePit = 0;
    }

    public int getSum() {
        return pits.stream().mapToInt(Integer::intValue).sum();
    }

    public Integer set(int index, Integer value) {
        return pits.set(index, value);
    }

    public Integer sowToMy(Integer index, Integer stones, BoardHalf opponentHalf) {
        ListIterator<Integer> iterator = pits.listIterator(index);
        ListIterator<Integer> oppositeIterator = opponentHalf.pits.listIterator(PITS_COUNT - index);
        while (stones > 0 && iterator.hasNext()) {
            Integer currentStones = iterator.next();
            Integer oppositeStones = oppositeIterator.previous();
            if (currentStones == 0 && stones == 1 && oppositeStones > 0) {
                largePit += stones + oppositeStones;
                oppositeIterator.set(0);
            } else {
                iterator.set(currentStones + 1);
            }
            stones--;
        }
        return stones;
    }

    public Integer sowToOpponent(Integer stones) {
        ListIterator<Integer> iterator = pits.listIterator();
        while (stones > 0 && iterator.hasNext()) {
            Integer currentStones = iterator.next();
            iterator.set(currentStones + 1);
            stones--;
        }
        return stones;
    }

    public Integer sowToLargePit(Integer stones) {
        largePit += 1;
        return --stones;
    }

    public void moveAllToLargePit() {
        for (int i = 0; i < pits.size(); i++) {
            largePit += pits.set(i, 0);
        }
    }

}
