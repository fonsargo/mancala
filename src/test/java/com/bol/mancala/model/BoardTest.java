package com.bol.mancala.model;

import com.bol.mancala.TestUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    @Test
    void shouldMakeSimpleMove() {
        Board board = new Board();
        board.makeMove(PlayerTurn.FIRST_PLAYER, 1);

        Board expected = TestUtils.createBoard(
                List.of(6, 0, 7, 7, 7, 7),
                1,
                List.of(7, 6, 6, 6, 6, 6),
                0,
                PlayerTurn.SECOND_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatMoveIfEndsInKalah() {
        Board board = new Board();
        board.makeMove(PlayerTurn.FIRST_PLAYER, 0);

        Board expected = TestUtils.createBoard(
                List.of(0, 7, 7, 7, 7, 7),
                1,
                List.of(6, 6, 6, 6, 6, 6),
                0,
                PlayerTurn.FIRST_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldMakeCapture() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(0, 2, 0, 0, 0, 0),
                0,
                Lists.newArrayList(0, 0, 5, 3, 0, 0),
                0,
                PlayerTurn.FIRST_PLAYER,
                null);
        board.makeMove(PlayerTurn.FIRST_PLAYER, 1);

        Board expected = TestUtils.createBoard(
                List.of(0, 0, 1, 0, 0, 0),
                6,
                List.of(0, 0, 0, 3, 0, 0),
                0,
                PlayerTurn.SECOND_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatAroundTheBoardIfMoreThan12Stones() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(14, 5, 3, 2, 7, 0),
                4,
                Lists.newArrayList(4, 2, 5, 0, 8, 0),
                3,
                PlayerTurn.FIRST_PLAYER,
                null);
        board.makeMove(PlayerTurn.FIRST_PLAYER, 0);

        Board expected = TestUtils.createBoard(
                List.of(1, 7, 4, 3, 8, 1),
                5,
                List.of(5, 3, 6, 1, 9, 1),
                3,
                PlayerTurn.SECOND_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatAroundTheBoardAndCapture() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(13, 5, 3, 2, 7, 0),
                4,
                Lists.newArrayList(4, 2, 5, 0, 8, 0),
                3,
                PlayerTurn.FIRST_PLAYER,
                null);
        board.makeMove(PlayerTurn.FIRST_PLAYER, 0);

        Board expected = TestUtils.createBoard(
                List.of(0, 6, 4, 3, 8, 1),
                7,
                List.of(5, 3, 6, 1, 9, 0),
                3,
                PlayerTurn.SECOND_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldFinishGame() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(2, 5, 0, 2, 7, 0),
                40,
                Lists.newArrayList(0, 0, 0, 0, 0, 5),
                30,
                PlayerTurn.SECOND_PLAYER,
                null);
        board.makeMove(PlayerTurn.SECOND_PLAYER, 5);

        Board expected = TestUtils.createBoard(
                List.of(0, 0, 0, 0, 0, 0),
                60,
                List.of(0, 0, 0, 0, 0, 0),
                31,
                PlayerTurn.FIRST_PLAYER,
                GameResult.WIN
                );
        assertThat(board).isEqualTo(expected);
    }
}
