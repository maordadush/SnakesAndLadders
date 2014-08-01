/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.awt.Point;

/**
 *
 * @author Noam
 */
public class BoardSquare extends Point {
    private eChars type = eChars.NONE;
    private int squareNumber; 
    private BoardSquare jumpTo;

    BoardSquare(int squareNumber) {
       this.squareNumber = squareNumber;
    }


    public BoardSquare getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(BoardSquare jumpTo) {
        this.jumpTo = jumpTo;
    }

    public int getSquareNumber() {
        return squareNumber;
    }

    public void setSquareNumber(int squareNumber) {
        this.squareNumber = squareNumber;
    }

    public eChars getType() {
        return type;
    }

    public void setType(eChars type) {
        this.type = type;
    }

}
