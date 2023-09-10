package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class PlayerBoard {

    private List<Integer> stonesInPits;
    private Integer largePitStones;

    public int pickStones(int pitIndex) {
        if (pitIndex > stonesInPits.size()) {
            throw new IllegalArgumentException("Can't get stones from pit with index: " + pitIndex + ", total pits size: " + stonesInPits.size());
        }
        Integer stones = stonesInPits.get(pitIndex);
        stonesInPits.set(pitIndex, 0);
        return stones;
    }

    public Pair<Integer, Integer> sowToMy(int stones, int startIndex) {
        for (int i = startIndex; i < stonesInPits.size(); i++) {
            stonesInPits.set(i, stonesInPits.get(i) + 1);
            stones--;
            if (stones == 0) {
                return Pair.of(stones, i);
            }
        }
        if (stones > 0) {
            largePitStones++;
            stones--;
        }
        return stones;
    }

    public int sowToOpponent(int stones) {
        for (int i = 0; i < stonesInPits.size() && stones > 0; i++) {
            stonesInPits.set(i, stonesInPits.get(i) + 1);
            stones--;
        }
        return stones;
    }

}
