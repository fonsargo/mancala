package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Board {

    private BoardHalf myHalf;
    private BoardHalf opponentHalf;

    public Board() {
        this.myHalf = new BoardHalf();
        this.opponentHalf = new BoardHalf();
    }
}
