package com.epam.rd.autocode.concurrenttictactoe;

import java.util.Arrays;

public class TicTacToeImpl implements TicTacToe {
    private final char[][] board;
    private char lastMark;
    private final Object lock = new Object();

    public TicTacToeImpl() {
        this.board = new char[][] {
                { ' ', ' ', ' ' },
                { ' ', ' ', ' ' },
                { ' ', ' ', ' ' }
        };
        this.lastMark = 'O'; // 'X' empieza
    }

    @Override
    public void setMark(int x, int y, char mark) {
        synchronized (lock) {
            if (board[x][y] != ' ') {
                throw new IllegalArgumentException("Cell already marked");
            }

            board[x][y] = mark;
            lastMark = mark;

            lock.notifyAll(); 
        }
    }

    @Override
    public char[][] table() {
        synchronized (lock) {
            char[][] copy = new char[3][3];
            for (int i = 0; i < 3; i++) {
                copy[i] = Arrays.copyOf(board[i], 3);
            }
            return copy;
        }
    }

    @Override
    public char lastMark() {
        synchronized (lock) {
            return lastMark;
        }
    }

    public static TicTacToe buildGame() {
        return new TicTacToeImpl();
    }
}