package com.bol.mancala.repository.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Game {

    @Id
    private UUID id;
    private GameStatus status;
    private String firstPlayerId;
    private String secondPlayerId;
    private Board board;
}
