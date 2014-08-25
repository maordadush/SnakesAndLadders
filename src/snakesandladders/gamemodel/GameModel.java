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
import snakesandladders.players.SinglePlayer;

/**
 *
 * @author Noam
 */
public class GameModel implements iWinChecker {

    public static final int MAX_PLAYERS = 4;
    public static final int NUM_OF_SOLDIERS = 4;
    private List<SinglePlayer> players;
    private final SnakesAndLaddersSingleGame game;
    private SinglePlayer currTurnPlayer;
    private String saveGamePath;
    private int m_NumOfPlayers;
    private int m_numOfSnakesAndLadders;
    private int m_CurrentPlayerIndex;
    private String m_GameName;
    private int m_GameNameIndex = 0;
    private int m_NumOfSoldiersToWin;

    public GameModel(int o_GameSize, int o_numOfSnakesAndLadders, int o_NumOfPlayers, int o_numOfSoldiersToWin) {
        if (o_numOfSnakesAndLadders > 1 || o_numOfSnakesAndLadders < (o_GameSize * o_GameSize) - 2) {
            m_numOfSnakesAndLadders = o_numOfSnakesAndLadders;
        } else {
            throw new UnsupportedOperationException("Illeagal number of snakes and ladders");
        }

        if (o_NumOfPlayers > 1 || o_NumOfPlayers < 5) {
            m_NumOfPlayers = o_NumOfPlayers;
            players = new ArrayList<>(m_NumOfPlayers);
        } else {
            throw new UnsupportedOperationException("Illeagal number of players");
        }
        setM_NumOfSoldiersToWin(o_numOfSoldiersToWin);
        game = new SnakesAndLaddersSingleGame(o_GameSize, o_numOfSnakesAndLadders);
        saveGamePath = null;
        m_GameName = "SnakesAndLadders_" + m_GameNameIndex;
        m_GameNameIndex++;
    }

    public void deinitPlayers() {
        players.clear();
    }

    @Override
    public boolean checkWinner(int numOfSoldiersToWin) {
        Boolean returnedValue = false;
        SinglePlayer winningPlayer = getWinnerPlayer(numOfSoldiersToWin);

        if (winningPlayer != null) {
            returnedValue = true;
        }

        return returnedValue;
    }

    public void initNewGame(boolean startNewGame) {
        game.initGame();
        if (startNewGame){
            game.shuffleSnakesAndLadders(m_numOfSnakesAndLadders);   
        }
        deinitPlayers();
    }

    public BoardSquare getCurrGameIndex() {
        return game.getCurrentBoardSquare();
    }

    public SinglePlayer getCurrPlayer() {
        return currTurnPlayer;
    }

    public void setCurrPlayer(SinglePlayer o_CurrPlayer) {
        currTurnPlayer = o_CurrPlayer;
    }

    public void setCurrPlayer(String o_CurrPlayerName) throws SnakesAndLaddersRunTimeException {
        SinglePlayer foundPlayer = null;

        for (SinglePlayer player : players) {
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

    public String getM_GameName() {
        return m_GameName;
    }

    public void setM_GameName(String m_GameName) {
        this.m_GameName = m_GameName;
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
    }

    public void selectFirstPlayer() {
        Random rand = new Random();
        int i = rand.nextInt(getNumOfPlayers());
        currTurnPlayer = getPlayers().get(i);
    }

    public SnakesAndLaddersSingleGame getGame() {
        return this.game;
    }

    public List<SinglePlayer> getPlayers() {
        return players;
    }

    public SinglePlayer getWinnerPlayer(int numOfSoldiersToWin) {
        SinglePlayer playerToReturn = null;
        for (SinglePlayer player : players) {
            if (player.getNumSoldiersAtSquare(game.getBoardSquare(game.getMAX_SQUARE_NUM()))
                    >= numOfSoldiersToWin) {
                playerToReturn = player;
            }
        }
        return playerToReturn;

    }

    public int getNumOfPlayers() {
        return m_NumOfPlayers;
    }

    public void addPlayer(SinglePlayer player) throws SnakesAndLaddersRunTimeException {
        if (players.size() < getNumOfPlayers()) {
            players.add(player);
        } else {
            throw new SnakesAndLaddersRunTimeException("addPlayer(): can't add more players.");
        }
    }

    public void setMove(SinglePlayer player, BoardSquare move) {
        player.getCurrentSoldier().setLocationOnBoard(move);
    }

    public SinglePlayer getPlayerByName(String name) {
        for (SinglePlayer player : getPlayers()) {
            if (player.getPlayerName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public int getM_NumOfSoldiersToWin() {
        return m_NumOfSoldiersToWin;
    }

    public void setM_NumOfSoldiersToWin(int m_NumOfSoldiersToWin) {
        if (m_NumOfSoldiersToWin < 1 || m_NumOfSoldiersToWin > 4) {
            throw new UnsupportedOperationException("Illeagal number of soldiers");
        }
        this.m_NumOfSoldiersToWin = m_NumOfSoldiersToWin;
    }

    public String getSaveGamePath() {
        return saveGamePath;
    }

    public void setSaveGamePath(String path) {
        saveGamePath = path;
    }

    public int getM_numOfSnakesAndLadders() {
        return m_numOfSnakesAndLadders;
    }
    
    public List<BoardSquare> getSnakesAndLaddersSquares(){
        List<BoardSquare> snakesAndLaddersList = new ArrayList();
        for (int i = 0; i <game.getO_BoardSize(); i++) {
            for (int j = 0; j < game.getO_BoardSize(); j++){
                BoardSquare currSquare = game.getBoardSquare(i, j);
                if(currSquare.getType() != eChars.NONE){
                  snakesAndLaddersList.add(currSquare);
                }
            }
                
        }
        return snakesAndLaddersList;
    }

    public void setPlayers(List<SinglePlayer> players) {
        this.players = players;
    }
}
