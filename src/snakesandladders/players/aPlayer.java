/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.players;

import java.util.ArrayList;
import java.util.List;

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
    
}
