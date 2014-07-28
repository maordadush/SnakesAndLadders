/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.players.aPlayer;
import snakesandladders.xml.XMLException;

/**
 *
 * @author Noam
 */
public class GameModel implements iWinChecker {

    public static final int MAX_PLAYERS = 4;
    public static final int NUM_OF_SOLDIERS = 4;
    private List<aPlayer> players;
    private SnakesAndLaddersSingleGame game;
    private aPlayer currTurnPlayer;
    private boolean selectNextGame;
    private String saveGamePath;
    private int m_NumOfPlayers;
    private int m_numOfSnakesAndLadders;
    private int m_CurrentPlayerIndex;

    public GameModel(int o_GameSize, int o_numOfSnakesAndLadders, int o_NumOfPlayers) {
        //TODO: move input valdition to here
        if (o_numOfSnakesAndLadders > 1 || o_numOfSnakesAndLadders < (o_GameSize * o_GameSize) - 2) {
            m_numOfSnakesAndLadders = o_numOfSnakesAndLadders;
        } else {
            throw new UnsupportedOperationException("Illeagal number of snakes and ladders");
        }

        if (o_NumOfPlayers > 1 || o_NumOfPlayers < 5) {
            m_NumOfPlayers = o_NumOfPlayers;
            players = new ArrayList<>();
        } else {
            throw new UnsupportedOperationException("Illeagal number of players"); //To change body of generated methods, choose Tools | Templates.

        }
        game = new SnakesAndLaddersSingleGame(o_GameSize, o_numOfSnakesAndLadders);
        saveGamePath = null;
    }

    public void InitPlayers() {
        players.clear();
    }

    @Override
    public boolean checkWinner(int numOfSoldiersToWin) {
        Boolean returnedValue = false;
        aPlayer winningPlayer = getWinnerPlayer(numOfSoldiersToWin);

        if (winningPlayer != null) {
            returnedValue = true;
        }

        return returnedValue;
    }

    public void initNewGame() {
        game.initGame();
        InitPlayers();
        selectNextGame = true;
    }

    public BoardSquare getCurrGameIndex() {
        return game.getCurrentBoardSquare();
    }

    public aPlayer getCurrPlayer() {
        return currTurnPlayer;
    }

    public void setCurrPlayer(aPlayer o_CurrPlayer) {
        currTurnPlayer = o_CurrPlayer;
    }

    public void setCurrPlayer(String o_CurrPlayerName) throws SnakesAndLaddersRunTimeException {
        aPlayer foundPlayer = null;
       
        for (aPlayer player : players) {
            if (player.getPlayerName().equals(o_CurrPlayerName)) {
                foundPlayer = player;
            }
        }
        if (foundPlayer != null) {
            this.currTurnPlayer = foundPlayer;
        } else {
            throw new SnakesAndLaddersRunTimeException("setCurrPlayer(): player found error.");
        }
    }

    public void forwardPlayer() {
        m_CurrentPlayerIndex = players.indexOf(getCurrPlayer());

        this.m_CurrentPlayerIndex++;
        if (this.m_CurrentPlayerIndex >= this.m_NumOfPlayers) {
            this.m_CurrentPlayerIndex = 0;
        }
        this.currTurnPlayer = getPlayers().get(m_CurrentPlayerIndex);
    }

    public void initGame() {
        game.initGame();
        selectFirstPlayer();
        selectNextGame = true;
    }

    public void selectFirstPlayer() {
        Random rand = new Random();
        int i = rand.nextInt(getNumOfPlayers());
        currTurnPlayer = getPlayers().get(i);
    }

    public boolean GetSelectNextGame() {
        return selectNextGame;
    }

    public SnakesAndLaddersSingleGame getGame() {
        return this.game;
    }

    public List<aPlayer> getPlayers() {
        return players;
    }

    public aPlayer getWinnerPlayer(int numOfSoldiersToWin) {
        aPlayer playerToReturn = null;
        for (aPlayer player : players) {
            if (player.getNumSoldiersAtSquare(game.getBoardSquare(game.getO_BoardSize() - 1, game.getO_BoardSize() - 1))
                    == numOfSoldiersToWin) {
                playerToReturn = player;
            }
        }
        return playerToReturn;

    }

    public int getNumOfPlayers() {
        return m_NumOfPlayers;
    }

    public SnakesAndLaddersSingleGame GetSingleGame() {
        return this.game;
    }

    public void addPlayer(aPlayer player) throws SnakesAndLaddersRunTimeException {
        if (players.size() < getNumOfPlayers()) {
            players.add(player);
        } else {
            throw new SnakesAndLaddersRunTimeException("addPlayer(): can't add more players.");
        }
    }

    public void setMove(aPlayer player, BoardSquare move) {
        player.getCurrentSoldier().setLocationOnBoard(move);
    }
}
