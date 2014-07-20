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
public class GameModel {

    public static final int MAX_PLAYERS = 4;
    private SnakesAndLaddersSingleGame game;
    private aPlayer currTurnPlayer;
    private boolean selectNextGame;
    private String saveGamePath;

    public GameModel(int o_GameSize, int o_NumOfPlayers) {
        game = new SnakesAndLaddersSingleGame(o_GameSize, o_NumOfPlayers);
        saveGamePath = null;
    }

    public void initNewGame() {
        game.initGame();
        game.InitPlayers();
        selectNextGame = true;
    }

    public boolean hasGameWon() {
        return (game.hasGameWon());
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

    private void selectFirstPlayer() {
        Random rand = new Random();
        int i = rand.nextInt(game.getNumOfPlayers());
        currTurnPlayer = game.getPlayers().get(i);
    }

}
