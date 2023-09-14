package com.bol.mancala.service.exception;

import java.util.UUID;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String playerId, UUID gameId) {
        super("Player with ID: " + playerId + " doesn't participate in this game with id: " + gameId);
    }
}