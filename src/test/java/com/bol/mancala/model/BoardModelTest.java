package com.bol.mancala.model;

import com.bol.mancala.TestUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardModelTest {

    @Test
    void shouldMakeSimpleMove() {
        BoardModel board = new BoardModel();
        board.makeMove(1);

        BoardModel expected = TestUtils.createBoard(
                List.of(6, 0, 7, 7, 7, 7),
                1,
                List.of(7, 6, 6, 6, 6, 6),
                0,
                PlayerTurn.SECOND_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatMoveIfEndsInKalah() {
        BoardModel board = new BoardModel();
        board.makeMove(0);

        BoardModel expected = TestUtils.createBoard(
                List.of(0, 7, 7, 7, 7, 7),
                1,
                List.of(6, 6, 6, 6, 6, 6),
                0,
                PlayerTurn.FIRST_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldMakeSimpleMoveFromSecondPlayer() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(0, 7, 7, 7, 7, 7),
                1,
                Lists.newArrayList(6, 6, 6, 6, 6, 6),
                0,
                PlayerTurn.SECOND_PLAYER);
        board.makeMove(4);

        BoardModel expected = TestUtils.createBoard(
                List.of(1, 8, 8, 8, 7, 7),
                1,
                List.of(6, 6, 6, 6, 0, 7),
                1,
                PlayerTurn.FIRST_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatMoveIfEndsInKalahSecondPlayer() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(0, 7, 7, 7, 7, 7),
                1,
                Lists.newArrayList(6, 6, 6, 6, 6, 6),
                0,
                PlayerTurn.SECOND_PLAYER);
        board.makeMove(0);

        BoardModel expected = TestUtils.createBoard(
                List.of(0, 7, 7, 7, 7, 7),
                1,
                List.of(0, 7, 7, 7, 7, 7),
                1,
                PlayerTurn.SECOND_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldMakeCapture() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(0, 2, 0, 0, 0, 0),
                0,
                Lists.newArrayList(0, 0, 5, 3, 0, 0),
                0,
                PlayerTurn.FIRST_PLAYER);
        board.makeMove(1);

        BoardModel expected = TestUtils.createBoard(
                List.of(0, 0, 1, 0, 0, 0),
                6,
                List.of(0, 0, 0, 3, 0, 0),
                0,
                PlayerTurn.SECOND_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldMakeCaptureForSecondPlayer() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(1, 0, 8, 0, 0, 1),
                10,
                Lists.newArrayList(3, 4, 5, 0, 7, 5),
                10,
                PlayerTurn.SECOND_PLAYER);
        board.makeMove(0);

        BoardModel expected = TestUtils.createBoard(
                List.of(1, 0, 0, 0, 0, 1),
                10,
                List.of(0, 5, 6, 0, 7, 5),
                19,
                PlayerTurn.FIRST_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatAroundTheBoardIfMoreThan12Stones() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(14, 5, 3, 2, 7, 0),
                4,
                Lists.newArrayList(4, 2, 5, 0, 8, 0),
                3,
                PlayerTurn.FIRST_PLAYER);
        board.makeMove(0);

        BoardModel expected = TestUtils.createBoard(
                List.of(1, 7, 4, 3, 8, 1),
                5,
                List.of(5, 3, 6, 1, 9, 1),
                3,
                PlayerTurn.SECOND_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldRepeatAroundTheBoardAndCapture() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(13, 5, 3, 2, 7, 0),
                4,
                Lists.newArrayList(4, 2, 5, 0, 8, 0),
                3,
                PlayerTurn.FIRST_PLAYER);
        board.makeMove(0);

        BoardModel expected = TestUtils.createBoard(
                List.of(0, 6, 4, 3, 8, 1),
                7,
                List.of(5, 3, 6, 1, 9, 0),
                3,
                PlayerTurn.SECOND_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldNotCaptureIfOppositePitIsEmpty() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(13, 2, 0, 0, 7, 0),
                4,
                Lists.newArrayList(4, 2, 0, 0, 8, 0),
                3,
                PlayerTurn.FIRST_PLAYER);
        board.makeMove(1);

        BoardModel expected = TestUtils.createBoard(
                List.of(13, 0, 1, 1, 7, 0),
                4,
                List.of(4, 2, 0, 0, 8, 0),
                3,
                PlayerTurn.SECOND_PLAYER);
        assertThat(board).isEqualTo(expected);
    }

    @Test
    void shouldFinishGame() {
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(0, 0, 0, 0, 0, 5),
                30,
                Lists.newArrayList(2, 5, 0, 2, 7, 0),
                40,
                PlayerTurn.FIRST_PLAYER);
        board.makeMove(5);

        BoardModel expected = TestUtils.createBoard(
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
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(3, 4, 5, 0, 7, 5),
                10,
                Lists.newArrayList(0, 0, 8, 0, 0, 0),
                10,
                PlayerTurn.FIRST_PLAYER);
        board.makeMove(0);

        BoardModel expected = TestUtils.createBoard(
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
        BoardModel board = TestUtils.createBoard(
                Lists.newArrayList(3, 4, 5, 0, 7, 5),
                8,
                Lists.newArrayList(0, 0, 0, 0, 0, 5),
                35,
                PlayerTurn.SECOND_PLAYER);
        board.makeMove(5);

        BoardModel expected = TestUtils.createBoard(
                List.of(0, 0, 0, 0, 0, 0),
                36,
                List.of(0, 0, 0, 0, 0, 0),
                36,
                PlayerTurn.FIRST_PLAYER,
                WinnerType.DRAW);
        assertThat(board).isEqualTo(expected);
    }
}
