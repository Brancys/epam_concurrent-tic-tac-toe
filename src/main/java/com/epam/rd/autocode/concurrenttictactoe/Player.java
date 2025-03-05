package com.epam.rd.autocode.concurrenttictactoe;

public interface Player extends Runnable {
    public static Player createPlayer(TicTacToe ticTacToe, char mark, PlayerStrategy strategy) {
        return new PlayerImpl(ticTacToe, mark, strategy);
    }
}
