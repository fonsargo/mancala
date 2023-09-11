package com.bol.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    private Long id;
    private GameStatus status;
    private Board board;
    private String firstPlayerId;
    private String secondPlayerId;

    public Game(String firstPlayerId) {
        this.status = GameStatus.NEW;
        this.board = new Board();
        this.firstPlayerId = firstPlayerId;
    }

    public void addSecondPlayer(String playerId) {
        if (status != GameStatus.NEW) {
            throw new IllegalArgumentException("Can't add player to game in status: " + status);
        }

        secondPlayerId = playerId;
        status = GameStatus.IN_PROGRESS;
    }


    public void makeMove(String playerId, int pitIndex) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Can't make move, game in status: " + status);
        }

        if (!firstPlayerId.equals(playerId) && !secondPlayerId.equals(playerId)) {
            throw new IllegalArgumentException("Player with ID: " + playerId + " doesn't participate in game with id: " + id);
        }

        PlayerTurn playerTurn = firstPlayerId.equals(playerId) ? PlayerTurn.FIRST_PLAYER : PlayerTurn.SECOND_PLAYER;

        board.makeMove(playerTurn, pitIndex);
        if (board.getWinner() != null) {
            status = GameStatus.FINISHED;
        }
    }

}
