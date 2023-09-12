package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bol.mancala.service.GameEngine.INITIAL_STONES_COUNT;
import static com.bol.mancala.service.GameEngine.PITS_COUNT;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BoardHalf {

    private List<Integer> pits;
    private Integer largePit;

    public BoardHalf() {
        this.pits = IntStream.range(0, PITS_COUNT).map(operand -> INITIAL_STONES_COUNT).boxed().collect(Collectors.toList());
        this.largePit = 0;
    }

    public Integer get(int index) {
        return pits.get(index);
    }

    public Integer getSum() {
        return pits.stream().mapToInt(Integer::intValue).sum();
    }

    public Integer set(int index, Integer value) {
        return pits.set(index, value);
    }

    public Integer sowToLargePit(Integer stones) {
        largePit += 1;
        return --stones;
    }

    public void addToLargePit(Integer stones) {
        largePit += stones;
    }

    public void moveAllToLargePit() {
        for (int i = 0; i < pits.size(); i++) {
            largePit += pits.set(i, 0);
        }
    }

}
