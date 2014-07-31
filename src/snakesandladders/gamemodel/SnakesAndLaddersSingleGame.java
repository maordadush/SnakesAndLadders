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
public class SnakesAndLaddersSingleGame {

    private int m_BoardSize = 0;
    private BoardSquare m_GameBoard[][];
    private int gameWinner;
    private BoardSquare m_CurrentSquare;
    private int m_numOfSnakesAndLadders;

    public SnakesAndLaddersSingleGame(int o_BoardSize, int o_numOfSnakesAndLadders) {
        if (o_BoardSize > 4 || o_BoardSize < 9){
            m_BoardSize = o_BoardSize;
            m_GameBoard = new BoardSquare[m_BoardSize][m_BoardSize];
            m_numOfSnakesAndLadders = o_numOfSnakesAndLadders;
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
            for (int j = 0; j < m_BoardSize ; j++) {
                m_GameBoard[i][j] = new BoardSquare((m_BoardSize * i) + j + 1);
            }
        }
        m_CurrentSquare = m_GameBoard[0][0];
        
        gameWinner = -1;
    }

    public void shuffleSnakesAndLadders(int o_NumOfSnakesAndLadders) {
        Random rand = new Random();
        int maxBoundary = this.m_BoardSize - 1;
        for (int i = 0; i < o_NumOfSnakesAndLadders; i++) {
            //snake head
            int X = rand.nextInt(maxBoundary - 1) + 1;
            int Y = rand.nextInt(maxBoundary);
            while ((X == 0 && Y == 0) || (X == maxBoundary && Y == maxBoundary)
                    || (m_GameBoard[X][Y].getType() != eChars.NONE)) {
                X = rand.nextInt(maxBoundary);
                Y = rand.nextInt(maxBoundary);
            }
            //snake tail 
            int nextX = X > 1 ? rand.nextInt(X) : 0;
            int nextY = rand.nextInt(maxBoundary);
            while ((nextX == 0 && nextY == 0) || (m_GameBoard[nextX][nextY].getType() != eChars.NONE)) {
                nextX = X > 1 ? rand.nextInt(X) : 0;
                nextY = rand.nextInt(maxBoundary);
            }

            // set snake paramter tail
            m_GameBoard[X][Y].setType(eChars.SNAKE_HEAD);
            m_GameBoard[X][Y].setJumpTo(m_GameBoard[nextX][nextY]);
            m_GameBoard[nextX][nextY].setType(eChars.SNAKE_TAIL);
            m_GameBoard[X][Y].setLocation(nextX, nextY);

        }
        
        for (int i = 0; i < o_NumOfSnakesAndLadders; i++) {
            //Ladder tail
            int X = rand.nextInt(maxBoundary - 1);
            int Y = rand.nextInt(maxBoundary);
            while ((X == 0 && Y == 0) || (m_GameBoard[X][Y].getType() != eChars.NONE)) {
                X = rand.nextInt(maxBoundary - 1);
                Y = rand.nextInt(maxBoundary);
            }
            //Ladder head 
            int nextX = rand.nextInt((maxBoundary) - (X + 1)) + (X + 1);
            int nextY = rand.nextInt(maxBoundary);
            while ((nextX == maxBoundary && nextY == maxBoundary) || (m_GameBoard[nextX][nextY].getType() != eChars.NONE)) {
                nextX = rand.nextInt((maxBoundary) - (X + 1)) + (X + 1);
                nextY = rand.nextInt(maxBoundary);
            }

            // set Ladder paramter tail
            m_GameBoard[X][Y].setType(eChars.LADDER_TAIL);
            m_GameBoard[X][Y].setJumpTo(m_GameBoard[nextX][nextY]);
            m_GameBoard[nextX][nextY].setType(eChars.LADDER_HEAD);
            m_GameBoard[X][Y].setLocation(nextX, nextY);

        }
    }

    public BoardSquare getCurrentBoardSquare() {
        return m_CurrentSquare;
    }
    
    public void setCurrentBoardSquare(BoardSquare updatedBoardSquere) {
        m_CurrentSquare = updatedBoardSquere;
    }

    public int getO_BoardSize() {
        return m_BoardSize;
    }

    public BoardSquare getBoardSquare(int i, int j) {
        return m_GameBoard[i][j];
    }

    public BoardSquare[][] getGame() {
        return m_GameBoard;
    }
    
}
