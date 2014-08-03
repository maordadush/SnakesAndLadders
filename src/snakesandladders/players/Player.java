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
import java.util.concurrent.atomic.AtomicInteger;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.BoardSquare;

/**
 *
 * @author Noam
 */
public class Player {

    public final int NUM_OF_SOLDIERS = 4;
    String m_PlayerName;
    List<Soldier> m_SoldiersList;
    static AtomicInteger nextId = new AtomicInteger();
    private int playerID;
    Color color;
    int m_CurrentSoldierIndex;
    private Soldier m_CurrentSoldier;
    ePlayerType type;

    public Player(String o_Name, ePlayerType type) {
        this.m_PlayerName = o_Name;
        this.type = type;
        m_SoldiersList = new ArrayList<>(NUM_OF_SOLDIERS);
        this.color = Color.decode(Integer.toString(this.hashCode()));
        this.m_CurrentSoldierIndex = 0;
        //this.m_CurrentSoldier = m_SoldiersList<m_CurrentSoldierIndex];
        playerID = nextId.incrementAndGet();
    }

    public String getPlayerName() {
        return m_PlayerName;
    }
    public int getPlayerID() {
        return playerID;
    }

    public List<Soldier> getM_SoldiersList() {
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

    public void setCurrentSoldier(int indexOfSoldier) throws SnakesAndLaddersRunTimeException {
        if (indexOfSoldier < 0 || indexOfSoldier > NUM_OF_SOLDIERS - 1) {
            throw new SnakesAndLaddersRunTimeException("setCurrentSoldier(): Invalid index.");
        }
        m_CurrentSoldier = m_SoldiersList.get(indexOfSoldier - 1);
    }

    public Soldier GetCurrentSoldier() {
        return m_CurrentSoldier;
    }

    
    public ePlayerType getType() {
        return type;
    }

    public int randomizeCurrentPlayer() throws SnakesAndLaddersRunTimeException {
        Random rand = new Random();
        int randomSoldierIndex;
        do {
            randomSoldierIndex = rand.nextInt(3) + 1;

            setCurrentSoldier(randomSoldierIndex);
        } while (m_SoldiersList.get(randomSoldierIndex - 1).m_FinishedGame);

        return randomSoldierIndex;
    }

    public void placeSoldierOnBoard(BoardSquare location) throws SnakesAndLaddersRunTimeException {
        if (!isSoldierListInited()) {
            throw new SnakesAndLaddersRunTimeException("placeSoldierOnBoard: soldiers list is not initilaize");
        }
        for (Soldier soldier : m_SoldiersList) {
            if (soldier.getLocationOnBoard().getSquareNumber() == 1) {
                soldier.setLocationOnBoard(location);
                break;
            }
        }
    }
    public Soldier getSoldier(int i) throws SnakesAndLaddersRunTimeException {
        if (i < 0 || i > NUM_OF_SOLDIERS - 1) {
            throw new SnakesAndLaddersRunTimeException("getSoldier: Illegal number of soldiers");
        }
        return this.getM_SoldiersList().get(i);
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder playerSoldiersString = new StringBuilder();
        playerSoldiersString.append("Player: " + this.getPlayerName() + "\t");
        for (Soldier soldier : m_SoldiersList) {
            playerSoldiersString.append("Soldier " + soldier.getSoldierID() + ": "
                    + (String.format("%02d", soldier.getLocationOnBoard().getSquareNumber()) + "\t"));
        }
        playerSoldiersString.append(System.lineSeparator());
        return playerSoldiersString.toString();
    }
    
    public void initSoldiers(BoardSquare location){
        int solderID = 1;
        for (int i = 0; i < NUM_OF_SOLDIERS; i++) {
            m_SoldiersList.add(new Soldier(this.getColor(), solderID++, location));
            
        }
    }

    private boolean isSoldierListInited() {
        return m_SoldiersList.size() == NUM_OF_SOLDIERS;
    }

}
