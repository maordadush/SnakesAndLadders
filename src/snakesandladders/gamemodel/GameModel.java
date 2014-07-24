/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.util.ArrayList;
import java.util.Random;
import snakesandladders.players.aPlayer;

/**
 *
 * @author Noam
 */
public class GameModel implements iWinChecker {

    public static final int MAX_PLAYERS = 4;
    private ArrayList<aPlayer> players;
    private SnakesAndLaddersSingleGame game;
    private aPlayer currTurnPlayer;
    private boolean selectNextGame;
    private String saveGamePath;

    public GameModel(int o_GameSize, int o_NumOfPlayers) {
        if (o_NumOfPlayers > 1 || o_NumOfPlayers < 5) {
            players = new ArrayList<>(o_NumOfPlayers);
        } else {
            throw new UnsupportedOperationException("Illeagal number of players"); //To change body of generated methods, choose Tools | Templates.

        }
        game = new SnakesAndLaddersSingleGame(o_GameSize);
        saveGamePath = null;
    }

    public void InitPlayers() {
        players.clear();
    }
    
    @Override
    public boolean checkWinner() {
        Boolean returnedValue = false;
        aPlayer winningPlayer = getWinnerPlayer();
        
        if (winningPlayer != null){
            returnedValue = true;
        }
        
        return returnedValue;
    }

    public boolean hasGameWon() {
        boolean returnValue = false;
        for (aPlayer player : players) {
            if (player.getNumOfSoldiersToWin() == 0) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public void initNewGame() {
        game.initGame();
        InitPlayers();
        selectNextGame = true;
    }

    public BoardSquare getCurrGameIndex() {
        return game.getCurrentBoardSquere();
    }

    public aPlayer getCurrPlayer() {
        return currTurnPlayer;
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

    public ArrayList<aPlayer> getPlayers() {
        return players;
    }

    public aPlayer getWinnerPlayer() {
        aPlayer playerToReturn = null;
        for (aPlayer player : players) {
            if (player.getNumSoldiersAtSquare(game.getBoardSquare(game.getO_BoardSize() - 1, game.getO_BoardSize() - 1))
                    == player.getNumOfSoldiersToWin()) {
                playerToReturn = player;
            }
        }
        return playerToReturn;

    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public SnakesAndLaddersSingleGame GetSingleGame() {
        return this.game;
    }

    public void SetPlayers(ArrayList<aPlayer> o_InitializedPlayers) {
        players = o_InitializedPlayers;
    }
}
