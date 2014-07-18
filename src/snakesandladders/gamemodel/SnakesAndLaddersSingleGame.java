/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.util.Random;
import snakesandladders.players.aPlayer;

/**
 *
 * @author Noam
 */
public class SnakesAndLaddersSingleGame implements iWinChecker {

    private int m_BoardSize = 0;
    private final eChars m_GameBoard[][];
    private int gameWinner;

    public SnakesAndLaddersSingleGame(int o_BoardSize) {
        if (o_BoardSize > 4 || o_BoardSize < 8) {
            m_BoardSize = o_BoardSize;
            m_GameBoard = new eChars[m_BoardSize][m_BoardSize];
        } else {
            throw new UnsupportedOperationException("Illeagal board size"); //To change body of generated methods, choose Tools | Templates.

        }

        gameWinner = -1;
    }

    public void setO_BoardSize(int o_BoardSize) {
        this.m_BoardSize = o_BoardSize;
    }

    public void initGame() {
        for (int i = 0; i < m_BoardSize; i++) {
            for (int j = 0; j < m_BoardSize; j++) {
                m_GameBoard[i][j] = eChars.NONE;
            }
        }
        gameWinner = -1;
    }

    public void shuffleSnakesAndLadders(int o_NumOfSnakesAndLadders) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int throwCube() {
        Random rand = new Random();

        int randomNum = rand.nextInt(6) + 1;

        return randomNum;
    }

    public void makeMove(aPlayer o_CurrentPlayer, int o_indexOfSoldier) {
        int cubeAnswer = throwCube();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkWinner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
