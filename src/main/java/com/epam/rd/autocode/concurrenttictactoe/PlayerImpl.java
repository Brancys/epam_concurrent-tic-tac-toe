package com.epam.rd.autocode.concurrenttictactoe;

public class PlayerImpl implements Player {
    private final TicTacToe game;
    private final char mark;
    private final PlayerStrategy strategy;
    static boolean isStarted = false;

    public PlayerImpl(TicTacToe game, char mark, PlayerStrategy strategy) {
        this.game = game;
        this.mark = mark;
        this.strategy = strategy;
    }

    public static void setStarted() {
        isStarted = true;
    }

    @Override
    public void run() {
        setStarted();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (game) {
                    while (game.lastMark() == mark && !isGameOver(game.table())) {
                        game.wait();
                    }

                    if (isGameOver(game.table())) {
                        game.notifyAll();
                        return;
                    }

                    Move move = strategy.computeMove(mark, game);
                    if (move == null || game.table()[move.row][move.column] != ' ') {
                        game.notifyAll();
                        return;
                    }

                    game.setMark(move.row, move.column, mark);
                    game.notifyAll();
                }
                Thread.sleep(1); // Delay para el turno del otro jugador
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                return;
            }
        }
    }

    private boolean isGameOver(char[][] table) {
        return hasWinningLine(table) || isBoardFull(table);
    }

    private boolean hasWinningLine(char[][] table) {
        for (int i = 0; i < 3; i++) {
            if (isSameMark(table[i][0], table[i][1], table[i][2]) || // Fila
                    isSameMark(table[0][i], table[1][i], table[2][i])) { // Columna
                return true;
            }
        }
        return isSameMark(table[0][0], table[1][1], table[2][2]) || // Diagonal principal
                isSameMark(table[0][2], table[1][1], table[2][0]); // Diagonal secundaria
    }

    private boolean isBoardFull(char[][] table) {
        for (char[] row : table) {
            for (char cell : row) {
                if (cell == ' ')
                    return false;
            }
        }
        return true;
    }

    private boolean isSameMark(char a, char b, char c) {
        return a != ' ' && a == b && b == c;
    }
}