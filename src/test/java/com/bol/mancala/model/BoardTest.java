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
        board.makeMove(1, PlayerTurn.FIRST_PLAYER);

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
        board.makeMove(0, PlayerTurn.FIRST_PLAYER);

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
    void shouldMakeSimpleMoveFromSecondPlayer() {
        Board board = new Board();
        board.makeMove(4, PlayerTurn.SECOND_PLAYER);

        Board expected = TestUtils.createBoard(
                List.of(7, 7, 7, 7, 6, 6),
                0,
                List.of(6, 6, 6, 6, 0, 7),
                1,
                PlayerTurn.FIRST_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatMoveIfEndsInKalahSecondPlayer() {
        Board board = new Board();
        board.makeMove(0, PlayerTurn.SECOND_PLAYER);

        Board expected = TestUtils.createBoard(
                List.of(6, 6, 6, 6, 6, 6),
                0,
                List.of(0, 7, 7, 7, 7, 7),
                1,
                PlayerTurn.SECOND_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldMakeCapture() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(0, 2, 0, 0, 0, 0),
                0,
                Lists.newArrayList(0, 0, 5, 3, 0, 0),
                0);
        board.makeMove(1, PlayerTurn.FIRST_PLAYER);

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
    void shouldMakeCaptureForSecondPlayer() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(1, 0, 8, 0, 0, 1),
                10,
                Lists.newArrayList(3, 4, 5, 0, 7, 5),
                10);
        board.makeMove(0, PlayerTurn.SECOND_PLAYER);

        Board expected = TestUtils.createBoard(
                List.of(1, 0, 0, 0, 0, 1),
                10,
                List.of(0, 5, 6, 0, 7, 5),
                19,
                PlayerTurn.FIRST_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatAroundTheBoardIfMoreThan12Stones() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(14, 5, 3, 2, 7, 0),
                4,
                Lists.newArrayList(4, 2, 5, 0, 8, 0),
                3);
        board.makeMove(0, PlayerTurn.FIRST_PLAYER);

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
                3);
        board.makeMove(0, PlayerTurn.FIRST_PLAYER);

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
    void shouldNotCaptureIfOppositePitIsEmpty() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(13, 2, 0, 0, 7, 0),
                4,
                Lists.newArrayList(4, 2, 0, 0, 8, 0),
                3);
        board.makeMove(1, PlayerTurn.FIRST_PLAYER);

        Board expected = TestUtils.createBoard(
                List.of(13, 0, 1, 1, 7, 0),
                4,
                List.of(4, 2, 0, 0, 8, 0),
                3,
                PlayerTurn.SECOND_PLAYER,
                null);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldFinishGame() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(0, 0, 0, 0, 0, 5),
                30,
                Lists.newArrayList(2, 5, 0, 2, 7, 0),
                40);
        board.makeMove(5, PlayerTurn.FIRST_PLAYER);

        Board expected = TestUtils.createBoard(
                List.of(0, 0, 0, 0, 0, 0),
                31,
                List.of(0, 0, 0, 0, 0, 0),
                60,
                PlayerTurn.SECOND_PLAYER,
                WinnerType.SECOND_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldFinishGameWithCapture() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(3, 4, 5, 0, 7, 5),
                10,
                Lists.newArrayList(0, 0, 8, 0, 0, 0),
                10);
        board.makeMove(0, PlayerTurn.FIRST_PLAYER);

        Board expected = TestUtils.createBoard(
                List.of(0, 0, 0, 0, 0, 0),
                42,
                List.of(0, 0, 0, 0, 0, 0),
                10,
                PlayerTurn.SECOND_PLAYER,
                WinnerType.FIRST_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldFinishGameWithDraw() {
        Board board = TestUtils.createBoard(
                Lists.newArrayList(3, 4, 5, 0, 7, 5),
                8,
                Lists.newArrayList(0, 0, 0, 0, 0, 5),
                35);
        board.makeMove(5, PlayerTurn.SECOND_PLAYER);

        Board expected = TestUtils.createBoard(
                List.of(0, 0, 0, 0, 0, 0),
                36,
                List.of(0, 0, 0, 0, 0, 0),
                36,
                PlayerTurn.FIRST_PLAYER,
                WinnerType.DRAW);
        assertThat(board).isEqualTo(expected);
    }
}
