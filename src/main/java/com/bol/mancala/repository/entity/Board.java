package com.bol.mancala.repository.entity;

import com.bol.mancala.model.BoardModel;
import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    private List<Integer> firstPlayerPits;
    private int firstPlayerLargePit;
    private List<Integer> secondPlayerPits;
    private int secondPlayerLargePit;
    private PlayerTurn playerTurn;
    private WinnerType result;

    public static Board fromBoardModel(BoardModel model) {
        return new Board(model.getFirstPlayerHalf().getPits(), model.getFirstPlayerHalf().getLargePit(),
                model.getSecondPlayerHalf().getPits(), model.getSecondPlayerHalf().getLargePit(), model.getPlayerTurn(), model.getResult());
    }
}
