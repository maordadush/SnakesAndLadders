/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.util.ArrayList;
import java.util.Random;
import snakesandladders.players.aPlayer;


/**
 *
 * @author Noam
 */
public class SnakesAndLaddersSingleGame implements iWinChecker {

    private int m_BoardSize = 0;
    private BoardSquare m_GameBoard[][];
    private int gameWinner;
    private ArrayList<aPlayer> players;
    private BoardSquare m_CuurentSquere;

    public int getNumOfPlayers() {
        return players.size();
    }

    public ArrayList<aPlayer> getPlayers() {
        return players;
    }

    public SnakesAndLaddersSingleGame(int o_BoardSize, int o_NumberOfPlayers) {
        if ((o_BoardSize > 4 || o_BoardSize < 9) && (o_NumberOfPlayers > 1 || o_NumberOfPlayers < 5)) {
            players = new ArrayList<>(o_NumberOfPlayers);
            m_BoardSize = o_BoardSize;
            m_GameBoard = new BoardSquare[m_BoardSize][m_BoardSize];
        } else {
            throw new UnsupportedOperationException("Illeagal board/players number"); //To change body of generated methods, choose Tools | Templates.

        }

        gameWinner = -1;
    }

    public void InitPlayers() {
        players.clear();
    }

    public void setO_BoardSize(int o_BoardSize) {
        this.m_BoardSize = o_BoardSize;
    }

    public void initGame() {
        for (int i = 0; i < m_BoardSize; i++) {
            for (int j = 0; j < m_BoardSize; j++) {
                m_GameBoard[i][j] = new BoardSquare();
            }
        }
        gameWinner = -1;
    }

    public void shuffleSnakesAndLadders(int o_NumOfSnakesAndLadders) {
        Random rand = new Random();
        for (int i = 0; i < o_NumOfSnakesAndLadders; i++) {
            //snake head
            int X = rand.nextInt(this.m_BoardSize - 1);
            int Y = rand.nextInt(this.m_BoardSize - 1);
            while ((X == 0 && Y == 0) || (X == m_BoardSize - 1 && Y == m_BoardSize - 1)
                    || (m_GameBoard[X][Y].getType() != eChars.NONE)) {
                X = rand.nextInt(this.m_BoardSize - 1);
                Y = rand.nextInt(this.m_BoardSize - 1);
            }
            //snake tail 
            int nextX = rand.nextInt(m_BoardSize - X - 1);
            int nextY = rand.nextInt(m_BoardSize - 1);
            while ((X != 0 && Y != 0) || (m_GameBoard[X][Y].getType() != eChars.NONE)) {
                nextX = rand.nextInt(this.m_BoardSize - X - 1);
                nextY = rand.nextInt(this.m_BoardSize - 1);
            }

            // set snake paramter tail
            m_GameBoard[X][Y].setType(eChars.SNAKE_HEAD);
            m_GameBoard[X][Y].setJumpTo(m_GameBoard[nextX][nextY]);
            m_GameBoard[nextX][nextY].setType(eChars.SNAKE_TAIL);
            m_GameBoard[X][Y].setLocation(nextX, nextY);

        }
        
        for (int i = 0; i < o_NumOfSnakesAndLadders; i++) {
            //Ladder tail
            int X = rand.nextInt(this.m_BoardSize - 1);
            int Y = rand.nextInt(this.m_BoardSize - 1);
            while ((X == 0 && Y == 0) || (X == m_BoardSize - 1 && Y == m_BoardSize - 1)
                    || (m_GameBoard[X][Y].getType() != eChars.NONE)) {
                X = rand.nextInt(this.m_BoardSize - 1);
                Y = rand.nextInt(this.m_BoardSize - 1);
            }
            //Ladder head 
            int nextX = rand.nextInt(m_BoardSize + X + 1);
            int nextY = rand.nextInt(m_BoardSize - 1);
            while ((X != m_BoardSize - 1 && Y != m_BoardSize - 1 ) || (m_GameBoard[X][Y].getType() != eChars.NONE)) {
                nextX = rand.nextInt(this.m_BoardSize + X + 1);
                nextY = rand.nextInt(this.m_BoardSize - 1);
            }

            // set Ladder paramter tail
            m_GameBoard[X][Y].setType(eChars.LADDER_TAIL);
            m_GameBoard[X][Y].setJumpTo(m_GameBoard[nextX][nextY]);
            m_GameBoard[nextX][nextY].setType(eChars.LADDER_HEAD);
            m_GameBoard[X][Y].setLocation(nextX, nextY);

        }
    }

    private int throwCube() {
        Random rand = new Random();

        int randomNum = rand.nextInt(5) + 1;

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

    boolean hasGameWon() {
        boolean returnValue = false;
        for (aPlayer player : players) {
            if (player.getNumOfSoldiersToWin() == 0) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    BoardSquare getCurrentBoardSquere() {
        return m_CuurentSquere;
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
