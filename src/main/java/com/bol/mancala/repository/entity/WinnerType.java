package com.bol.mancala.repository.entity;

public enum WinnerType {
    FIRST_PLAYER,
    SECOND_PLAYER,
    DRAW,
    ;

    public static WinnerType fromTurn(PlayerTurn playerTurn) {
        return valueOf(playerTurn.name());
    }
}
