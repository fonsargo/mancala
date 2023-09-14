package com.bol.mancala.dto;

import com.bol.mancala.repository.entity.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    private BoardDto board;
    private boolean isFirstPlayer;
    private GameStatus status;
}
