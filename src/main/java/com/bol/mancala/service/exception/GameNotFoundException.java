package com.bol.mancala.service.exception;

import java.util.UUID;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(UUID uuid) {
        super("Game with UUID=" + uuid + " doesn't exist");
    }
}
