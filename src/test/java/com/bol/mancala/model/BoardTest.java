package com.bol.mancala.model;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    @Test
    void shouldMakeSimpleMove() {
        Board board = new Board();
        board.makeMove(1);

        Board expected = new Board(
                List.of(6, 0, 7, 7, 7, 7),
                1,
                List.of(7, 6, 6, 6, 6, 6),
                0,
                false,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatMoveIfEndsInKalah() {
        Board board = new Board();
        board.makeMove(0);

        Board expected = new Board(
                List.of(0, 7, 7, 7, 7, 7),
                1,
                List.of(6, 6, 6, 6, 6, 6),
                0,
                true,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldMakeCapture() {
        Board board = new Board(
                Lists.newArrayList(0, 2, 0, 0, 0, 0),
                0,
                Lists.newArrayList(0, 0, 5, 3, 0, 0),
                0);
        board.makeMove(1);

        Board expected = new Board(
                List.of(0, 0, 1, 0, 0, 0),
                6,
                List.of(0, 0, 0, 3, 0, 0),
                0,
                false,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatAroundTheBoardIfMoreThan12Stones() {
        Board board = new Board(
                Lists.newArrayList(14, 5, 3, 2, 7, 0),
                4,
                Lists.newArrayList(4, 2, 5, 0, 8, 0),
                3);
        board.makeMove(0);

        Board expected = new Board(
                List.of(1, 7, 4, 3, 8, 1),
                5,
                List.of(5, 3, 6, 1, 9, 1),
                3,
                false,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatAroundTheBoardAndCapture() {
        Board board = new Board(
                Lists.newArrayList(13, 5, 3, 2, 7, 0),
                4,
                Lists.newArrayList(4, 2, 5, 0, 8, 0),
                3);
        board.makeMove(0);

        Board expected = new Board(
                List.of(0, 6, 4, 3, 8, 1),
                7,
                List.of(5, 3, 6, 1, 9, 0),
                3,
                false,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldNotCaptureIfOppositePitIsEmpty() {
        Board board = new Board(
                Lists.newArrayList(13, 2, 0, 0, 7, 0),
                4,
                Lists.newArrayList(4, 2, 0, 0, 8, 0),
                3);
        board.makeMove(1);

        Board expected = new Board(
                List.of(13, 0, 1, 1, 7, 0),
                4,
                List.of(4, 2, 0, 0, 8, 0),
                3,
                false,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldFinishGame() {
        Board board = new Board(
                Lists.newArrayList(0, 0, 0, 0, 0, 5),
                30,
                Lists.newArrayList(2, 5, 0, 2, 7, 0),
                40);
        board.makeMove(5);

        Board expected = new Board(
                List.of(0, 0, 0, 0, 0, 0),
                31,
                List.of(0, 0, 0, 0, 0, 0),
                60,
                false,
                GameResult.LOSE);
        assertThat(board).isEqualTo(expected);
    }
}
