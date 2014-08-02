/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.players;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
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
    private int soldierID;
    
    public Soldier(Color color, int soldierID, BoardSquare location) {
        this.locationOnBoard = location;
        this.color = color;
        this.soldierID = soldierID;
    }

    public int getSoldierID() {
        return soldierID;
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
