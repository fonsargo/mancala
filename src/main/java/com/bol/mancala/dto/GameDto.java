package com.bol.mancala.dto;

import com.bol.mancala.model.GameResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private Long id;
    private List<Integer> myPits;
    private int myLargePit;
    private List<Integer> opponentPits;
    private int opponentLargePit;
    private boolean isMyTurn;
    private GameResult result;
}
