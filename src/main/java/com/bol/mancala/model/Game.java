package com.bol.mancala.model;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Accessors(chain = true)
public class Game {

    public static final int MAX_PLAYERS = 2;
    public static final int PITS_COUNT = 6;
    public static final int INITIAL_STONES_COUNT = 6;

    private static final Random random = new Random();

    @Id
    private Long id;
    private GameStatus status = GameStatus.NEW;
    private Map<Long, PlayerBoard> playerBoards = new HashMap<>();
    private Long playerTurnId;

    public Game addPlayer(long playerId) {
        if (status != GameStatus.NEW) {
            throw new IllegalArgumentException("Can't add player to game in status: " + status);
        }

        PlayerBoard playerBoard = new PlayerBoard(
                IntStream.range(0, PITS_COUNT).map(operand -> INITIAL_STONES_COUNT).boxed().collect(Collectors.toList()),
                0
        );
        playerBoards.put(playerId, playerBoard);
        if (playerBoards.size() == MAX_PLAYERS) {
            status = GameStatus.IN_PROGRESS;
            playerTurnId = playerBoards.keySet().stream().skip(random.nextInt(MAX_PLAYERS)).findFirst().orElse(playerId);
        }
        return this;
    }

    public Game makeMove(long playerId, int pitIndex) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Can't make move, game in status: " + status);
        }

        if (playerId != playerTurnId) {
            //TODO player name?
            throw new IllegalArgumentException("Can't make move, it's not your turn. Next player is: " + playerId);
        }

        PlayerBoard myBoard = playerBoards.get(playerId);
        int stones = myBoard.pickStones(pitIndex);
        if (stones == 0) {
            throw new IllegalArgumentException("Can't pick up stones from pit with index: " + pitIndex + ", it's empty!");
        }

        Iterator<Map.Entry<Long, PlayerBoard>> iterator = Iterables.cycle(playerBoards.entrySet()).iterator();
        Map.Entry<Long, PlayerBoard> boardEntry = iterator.next();
        while(boardEntry.getKey() != playerId) {
            boardEntry = iterator.next();
        }
        int startIndex = pitIndex + 1;
        while (stones > 0) {
            PlayerBoard playerBoard = boardEntry.getValue();
            if (boardEntry.getKey() == playerId) {
                stones = playerBoard.sowToMy(stones, startIndex);
                startIndex = 0;

            } else {
                stones = playerBoard.sowToOpponent(stones);
            }
            boardEntry = iterator.next();
        }

        return this;
    }

}
