/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import snakesandladders.gamemodel.BoardSquare;

/**
 *
 * @author Noam
 */
public abstract class aPlayer {

    public final int NUM_OF_SOLDIERS = 4;
    String m_PlayerName;
    Soldier[] m_SoldiersList;
    int m_NumOfSoldiersToWin;
    Color color;

    protected aPlayer(String o_Name, int o_NumOfSoldiersToWin) {
        this.m_PlayerName = o_Name;
        this.m_NumOfSoldiersToWin = o_NumOfSoldiersToWin;
        m_SoldiersList = new Soldier[NUM_OF_SOLDIERS];
        this.color = Color.decode(Integer.toString(this.hashCode()));
        for (int i = 0; i < m_SoldiersList.length; i++) {
             m_SoldiersList[i] = new Soldier(this.color);
        }
    }

    public String getPlayerName() {
        return m_PlayerName;
    }

    public int getNumOfSoldiersToWin() {
        return m_NumOfSoldiersToWin;
    }

    public void setNumOfSoldiersToWin(int m_NumOfSoldiersToWin) {
        this.m_NumOfSoldiersToWin = m_NumOfSoldiersToWin;
    }

    public Soldier[] getM_SoldiersList() {
        return m_SoldiersList;
    }


    public int getNumSoldiersAtSquare(BoardSquare bs) {
        int numSoldiers = 0;
        for (Soldier soldier : m_SoldiersList) {
            if (soldier.atSquare(bs)) {
                numSoldiers++;
            }
        }
        return numSoldiers;
    }
  
    
    
 
    
    
}
