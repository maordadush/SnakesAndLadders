/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.players;

import java.awt.Color;
import snakesandladders.gamemodel.BoardSquare;

/**
 *
 * @author Noam
 */
public class Soldier {
    BoardSquare m_IndexOnBoard;
    boolean m_FinishedGame;
    private BoardSquare locationOnBoard;
    Color color;

    public Soldier(Color color) {
        this.color = color;
        m_FinishedGame = false;
    }

    public boolean isM_FinishedGame() {
        return m_FinishedGame;
    }

    public void setM_FinishedGame(boolean m_FinishedGame) {
        this.m_FinishedGame = m_FinishedGame;
    }
    
    public BoardSquare getLocationOnBoard() {
        return locationOnBoard;
    }

    public void setLocationOnBoard(BoardSquare locationOnBoard) {
        this.locationOnBoard = locationOnBoard;
    }

    boolean atSquare(BoardSquare bs) {
      return bs == this.locationOnBoard;
    }
}
