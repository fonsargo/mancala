package com.bol.mancala;

import com.bol.mancala.repository.entity.Game;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;

import java.util.UUID;

@Configuration
public class DBConfiguration {

    @Bean
    BeforeConvertCallback<Game> beforeConvertCallback() {
        return game -> {
            if (game.getId() == null) {
                game.setId(UUID.randomUUID());
            }
            return game;
        };
    }
}
