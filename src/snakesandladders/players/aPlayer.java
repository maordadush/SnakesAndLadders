/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.BoardSquare;

/**
 *
 * @author Noam
 */
public abstract class aPlayer {

    public final int NUM_OF_SOLDIERS = 4;
    String m_PlayerName;
    Soldier[] m_SoldiersList;
    Color color;
    int m_CurrentSoldierIndex;
    private Soldier m_CurrentSoldier;

    protected aPlayer(String o_Name) {
        this.m_PlayerName = o_Name;
        m_SoldiersList = new Soldier[NUM_OF_SOLDIERS];
        this.color = Color.decode(Integer.toString(this.hashCode()));
        for (int i = 0; i < m_SoldiersList.length; i++) {
            m_SoldiersList[i] = new Soldier(this.color);
        }
        this.m_CurrentSoldierIndex = 0;
        this.m_CurrentSoldier = m_SoldiersList[m_CurrentSoldierIndex];

    }

    public String getPlayerName() {
        return m_PlayerName;
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

    public Soldier getCurrentSoldier() {
        return m_CurrentSoldier;
    }

    public void ForwardCurrentSoldier() {
        this.m_CurrentSoldierIndex++;
        if (this.m_CurrentSoldierIndex >= this.NUM_OF_SOLDIERS) {
            this.m_CurrentSoldierIndex = 0;
        }
        this.m_CurrentSoldier = this.m_SoldiersList[this.m_CurrentSoldierIndex];
    }

    public void setCurrentSoldier(int indexOfSoldier) throws SnakesAndLaddersRunTimeException {
        if (indexOfSoldier > 0 && indexOfSoldier < 5) {
            m_CurrentSoldier = m_SoldiersList[indexOfSoldier - 1];
        } else {
            throw new SnakesAndLaddersRunTimeException("setCurrentSoldier(): Invalid index.");
        }
    }

    public Soldier GetCurrentSoldier() {
        return m_CurrentSoldier;
    }

    public int randomizeCurrentPlayer() throws SnakesAndLaddersRunTimeException {
        Random rand = new Random();
        int randomSoldierIndex;
        do {
            randomSoldierIndex = rand.nextInt(4) + 1;

            setCurrentSoldier(randomSoldierIndex);
        } while (m_SoldiersList[randomSoldierIndex - 1].m_FinishedGame);

        return randomSoldierIndex;
    }
}
