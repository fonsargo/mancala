package com.bol.mancala.repository;

import com.bol.mancala.repository.entity.Game;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BeforeConvertGameEntityCallback implements BeforeConvertCallback<Game> {

    @Override
    public Game onBeforeConvert(Game game) {
        if (game.getId() == null) {
            game.setId(UUID.randomUUID());
        }
        return game;
    }
}
