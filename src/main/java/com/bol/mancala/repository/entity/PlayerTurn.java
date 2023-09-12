package com.bol.mancala.repository.entity;

public enum PlayerTurn {
    FIRST_PLAYER,
    SECOND_PLAYER,
    ;

    public PlayerTurn toggle() {
        return this == FIRST_PLAYER ? SECOND_PLAYER : FIRST_PLAYER;
    }
}
