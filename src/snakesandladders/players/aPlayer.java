/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.players;

import java.util.ArrayList;
import java.util.List;
import snakesandladders.gamemodel.BoardSquare;

/**
 *
 * @author Noam
 */
public abstract class aPlayer {

    String m_PlayerName;
    List<Soldier> m_SoldiersList;
    public final int NUM_OF_SOLDIERS = 4;
    int m_NumOfSoldiersToWin;

    protected aPlayer(String o_Name, int o_NumOfSoldiersToWin) {
        this.m_PlayerName = o_Name;
        this.m_NumOfSoldiersToWin = o_NumOfSoldiersToWin;
        this.m_SoldiersList = new ArrayList<Soldier>(NUM_OF_SOLDIERS);
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

    public List<Soldier> getSoldiersList() {
        return m_SoldiersList;
    }
    public int getNumSoldiersAtSquare(BoardSquare bs){
        int numSoldiers = 0;
        for (Soldier soldier : m_SoldiersList) {
            if (soldier.atSquare(bs))
                numSoldiers++;
        }
        return numSoldiers;
    }
}
