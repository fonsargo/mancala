package com.bol.mancala.model;

public enum PlayerTurn {
    FIRST_PLAYER {
        @Override
        public PlayerTurn toggle() {
            return SECOND_PLAYER;
        }
    },
    SECOND_PLAYER {
        @Override
        public PlayerTurn toggle() {
            return FIRST_PLAYER;
        }
    },
    ;

    public abstract PlayerTurn toggle();
}
