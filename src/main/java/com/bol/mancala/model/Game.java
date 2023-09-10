package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Game {

    @Id
    private Long id;
    private GameStatus status;
    private Board board;
    private Map<PlayerTurn, String> players;

    public Game() {
        this.status = GameStatus.NEW;
        this.board = new Board();
        this.players = new HashMap<>();
    }

    public void addPlayer(String playerId) {
        if (status != GameStatus.NEW) {
            throw new IllegalArgumentException("Can't add player to game in status: " + status);
        }

        if (players.size() == 0) {
            players.put(PlayerTurn.FIRST_PLAYER, playerId);
        } else {
            players.put(PlayerTurn.SECOND_PLAYER, playerId);
            status = GameStatus.IN_PROGRESS;
        }
    }


    public void makeMove(String playerId, int pitIndex) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Can't make move, game in status: " + status);
        }

        PlayerTurn playerTurn = players.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), playerId))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("Player with ID: " + playerId + " doesn't participate in game with id: " + id));

        board.makeMove(playerTurn, pitIndex);
        if (board.getWinner() != null) {
            status = GameStatus.FINISHED;
        }
    }

}
