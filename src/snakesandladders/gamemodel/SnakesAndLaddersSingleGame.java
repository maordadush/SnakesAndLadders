/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.util.Random;
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;

/**
 *
 * @author Noam
 */
public class SnakesAndLaddersSingleGame {

    private static final int MIN_SQUARE_NUM = 1;
    private int MAX_SQUARE_NUM;
    private int gameWinner;

    public static int getMIN_SQUARE_NUM() {
        return MIN_SQUARE_NUM;
    }

    public int getMAX_SQUARE_NUM() {
        return MAX_SQUARE_NUM;
    }
    private int m_BoardSize = 0;
    private BoardSquare m_GameBoard[][];
    private BoardSquare m_CurrentSquare;
    private int m_numOfSnakesAndLadders;

    public SnakesAndLaddersSingleGame(int o_BoardSize, int o_numOfSnakesAndLadders) {
        if (o_BoardSize > 4 || o_BoardSize < 9) {
            m_BoardSize = o_BoardSize;
            m_GameBoard = new BoardSquare[m_BoardSize][m_BoardSize];
            m_numOfSnakesAndLadders = o_numOfSnakesAndLadders;
            MAX_SQUARE_NUM = m_BoardSize * m_BoardSize;
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
                int squareNumber = (m_BoardSize * i) + j + 1;
                m_GameBoard[i][j] = new BoardSquare(squareNumber);
            }
        }
        m_CurrentSquare = m_GameBoard[0][0];

        gameWinner = -1;
    }

    public void shuffleSnakesAndLadders(int o_NumOfSnakesAndLadders) {

        for (int i = 0; i < o_NumOfSnakesAndLadders; i++) {
            boolean isSnakePlaced = setSnake(getRandomCell(), getRandomCell());
            while (!isSnakePlaced) {
                isSnakePlaced = setSnake(getRandomCell(), getRandomCell());
            }
            boolean isLadderPlaced = setLadder(getRandomCell(), getRandomCell());
            while (!isLadderPlaced) {
                isLadderPlaced = setLadder(getRandomCell(), getRandomCell());
            }
        }
    }

    public BoardSquare getCurrentBoardSquare() {
        return m_CurrentSquare;
    }

    public void setCurrentBoardSquare(SinglePlayer currPlayer, BoardSquare updatedBoardSquere) {
        boolean playerFound = false;
        Soldier currentSoldier = null;
        for (SinglePlayer player : m_CurrentSquare.getPlayers()) {
            if (player.getPlayerName().equals(currPlayer.getPlayerName())) {
                currentSoldier = player.GetCurrentSoldier();
                player.getM_SoldiersList().remove(player.GetCurrentSoldier());
            }
        }

        for (SinglePlayer player : updatedBoardSquere.getPlayers()) {
            if (player.getPlayerName().equals(currPlayer.getPlayerName())) {
                playerFound = true;
                if (currentSoldier != null) {
                    player.getM_SoldiersList().add(currentSoldier);
                }
            }
        }
        if (!playerFound) {
            SinglePlayer player = new SinglePlayer(currPlayer.getPlayerName(), currPlayer.getType());
            if (currentSoldier != null) {
                player.getM_SoldiersList().add(currentSoldier);
            }
            updatedBoardSquere.getPlayers().add(player);
        }

        m_CurrentSquare = updatedBoardSquere;
    }

    public int getO_BoardSize() {
        return m_BoardSize;
    }

//TODO maybe delete this function and replace with the next one
    public BoardSquare getBoardSquare(int i, int j) {
        return m_GameBoard[i][j];
    }

    public BoardSquare getBoardSquare(int squareNumber) {
        if (squareNumber < MIN_SQUARE_NUM || squareNumber > MAX_SQUARE_NUM) {
            return null;
        }
        int boardSize = getO_BoardSize();
        int x = (squareNumber - 1) / boardSize;
        int y = (squareNumber - 1) % boardSize;
        return m_GameBoard[x][y];
    }

    public BoardSquare[][] getGameBoard() {
        return m_GameBoard;
    }

    public BoardSquare getRandomCell() {

        Random rand = new Random();
        int minVaildSquare = 2;
        int maxVaildSquare = (m_BoardSize * m_BoardSize) - 1;

        int randomSquare = rand.nextInt(maxVaildSquare - minVaildSquare) + minVaildSquare;

        return getBoardSquare(randomSquare);

    }

    public boolean setSnake(BoardSquare src, BoardSquare dest) {

        int boardSize = getO_BoardSize();
        int srcSquareNumber = src.getSquareNumber();
        int destSquareNumber = dest.getSquareNumber();

        if (src == dest) {
            return false;
        }
        
        if(dest.getSquareNumber() > src.getSquareNumber()){
            return false;
        }
        
        if (src.getSquareNumber() > MAX_SQUARE_NUM || dest.getSquareNumber() > MAX_SQUARE_NUM){
            return false;
        }
        
        if ((src.getType() != eChars.NONE) || (dest.getType() != eChars.NONE)) {
            return false;
        }
        
        int srcX = (srcSquareNumber - 1) / boardSize;
        int destX = (destSquareNumber - 1) / boardSize;
        if (srcX == destX || srcSquareNumber < destSquareNumber || srcX == 0) {
            return false;
        }
        
        src.setType(eChars.SNAKE_HEAD);
        src.setJumpTo(dest);
        dest.setType(eChars.SNAKE_TAIL);

        return true;
    }

    public boolean setLadder(BoardSquare src, BoardSquare dest) {

        int boardSize = getO_BoardSize();
        int srcSquareNumber = src.getSquareNumber();
        int destSquareNumber = dest.getSquareNumber();

        if (src == dest) {
            return false;
        }
        
        if(dest.getSquareNumber() < src.getSquareNumber()){
            return false;
        }
        
        if (src.getSquareNumber() > MAX_SQUARE_NUM || dest.getSquareNumber() > MAX_SQUARE_NUM){
            return false;
        }
        
        if ((src.getType() != eChars.NONE) || (dest.getType() != eChars.NONE)) {
            return false;
        }
        int srcX = (srcSquareNumber - 1) / boardSize;
        int destX = (destSquareNumber - 1) / boardSize;
        if (srcX == destX || srcSquareNumber > destSquareNumber || srcX == boardSize - 1) {
            return false;
        }
        src.setType(eChars.LADDER_TAIL);
        src.setJumpTo(dest);
        dest.setType(eChars.LADDER_HEAD);

        return true;
    }

    public void setCurrentBoardSquare(BoardSquare move) {
        m_CurrentSquare = move;
    }

    public void copyLaddersAndSnakes(SnakesAndLaddersSingleGame oldGame) {
        for (int i = 0; i < oldGame.getO_BoardSize(); i++) {
            for (int j = 0; j < oldGame.getO_BoardSize(); j++) {
                if (oldGame.getGameBoard()[i][j].getType() != eChars.NONE){
                    this.getGameBoard()[i][j].setType(oldGame.getGameBoard()[i][j].getType());
                }
            }
        }
    }
}
