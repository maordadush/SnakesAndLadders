/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;


/**
 *
 * @author Noam
 */
public class BoardSquare{
    private eChars type = eChars.NONE;
    private int squareNumber; 
    private BoardSquare jumpTo;
    private List<SinglePlayer> players;


    BoardSquare(int squareNumber) {
       this.squareNumber = squareNumber;
       this.players = new ArrayList<SinglePlayer>();
    }



    public BoardSquare getJumpTo() {
        return jumpTo;
    }

    public List<SinglePlayer> getPlayers() {
        return players;
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
