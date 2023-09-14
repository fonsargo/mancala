package com.bol.mancala.dto;

import com.bol.mancala.model.PlayerTurn;
import com.bol.mancala.model.WinnerType;
import com.bol.mancala.repository.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private List<Integer> firstPlayerPits;
    private int firstPlayerLargePit;
    private List<Integer> secondPlayerPits;
    private int secondPlayerLargePit;
    private PlayerTurn playerTurn = PlayerTurn.FIRST_PLAYER;
    private WinnerType result;

    public static BoardDto fromBoard(Board board) {
        return new BoardDto(board.getFirstPlayerPits(), board.getFirstPlayerLargePit(), board.getSecondPlayerPits(),
                board.getSecondPlayerLargePit(), board.getPlayerTurn(), board.getResult());
    }
}
