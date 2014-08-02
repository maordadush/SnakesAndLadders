/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamecontrol;

import java.util.List;
import snakesandladders.consoleview.ConsoleView;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.Cube;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;
import snakesandladders.players.ComputerPlayer;
import snakesandladders.players.HumanPlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.aPlayer;
import snakesandladders.players.ePlayerType;
import snakesandladders.xml.XML;
import snakesandladders.xml.eXMLLoadStatus;

/**
 *
 * @author Noam
 */
public class GameControl {

    private ConsoleView m_consoleView;
    private GameModel m_gameModel;
    int m_NumOfSoldiersToWin;

    public GameControl() {
        m_consoleView = new ConsoleView();
    }

    public void Run() {
        eStartMenu mainOpt = eStartMenu.CHOOSE;
        try {
            while (mainOpt != eStartMenu.EXIT) {
                mainOpt = m_consoleView.GetMainOptionMenu();
                switch (mainOpt) {
                    case START_NEW_GAME:
                        mainOpt = startNewGame();
                        break;
                    case LOAD_GAME:
                        mainOpt = startLoadGame();
                        break;
                    case EXIT:
                        break;
                    default:
                        throw new SnakesAndLaddersRunTimeException("Run(): Invalid mainOpt input.");
                }
            }
        } catch (SnakesAndLaddersRunTimeException ex) {
            m_consoleView.printSnakesAndLaddersRunTimeExceptiom(ex);
        }
    }

    private eStartMenu startNewGame() throws SnakesAndLaddersRunTimeException {
        createNewGame();
        runGame();

        return eStartMenu.EXIT;
    }

    private void runGame() throws SnakesAndLaddersRunTimeException {
        eEndMenu endGameOption = eEndMenu.CHOOSE;
        eXMLLoadStatus loadStatus;

        while (endGameOption != eEndMenu.EXIT_GAME) {
            runSingleGame();
            endGameOption = eEndMenu.CHOOSE;

            while (endGameOption == eEndMenu.CHOOSE) {
                endGameOption = m_consoleView.getEndGameOption();
                switch (endGameOption) {
                    case RESTART_GAME:
                        m_gameModel.initGame();
                        m_gameModel.GetSingleGame().shuffleSnakesAndLadders(m_NumOfSoldiersToWin);
                        break;
                    case START_NEW_GAME:
                        createNewGame();
                        break;
                    case LOAD_GAME:
                        loadStatus = loadGame();
                        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                            endGameOption = eEndMenu.CHOOSE;
                        }
                        break;
                    case EXIT_GAME:
                        break;
                    default:
                        throw new SnakesAndLaddersRunTimeException("RunGame(): Invalid endGameOption input.");
                }
            }
        }
    }

    private void runSingleGame() throws SnakesAndLaddersRunTimeException {
        eGameMenu gameOption;
        aPlayer player;
        //BoardSquare currGameIndex;

        while (!hasGameWon()) {
            //currGameIndex = m_gameModel.getCurrGameIndex();
            player = m_gameModel.getCurrPlayer();

            m_consoleView.ClearScreen();
            m_consoleView.displayCurrPlayer(player);
            m_consoleView.printGame(GetSingleGame(), getPlayers());
            if (player instanceof ComputerPlayer) {
                makeMove();
                m_gameModel.forwardPlayer();
            } else {
                gameOption = eGameMenu.CHOOSE;
                while (gameOption == eGameMenu.CHOOSE) {
                    gameOption = m_consoleView.getGameOption();
                    switch (gameOption) {
                        case MAKE_MOVE:
                            makeMove();
                            m_gameModel.forwardPlayer();
                            break;
                        case SAVE_GAME:
                            //             saveGame();
                            gameOption = eGameMenu.CHOOSE;
                            break;
                        case SAVE_GAME_AS:
                            //           saveGameAs();
                            gameOption = eGameMenu.CHOOSE;
                            break;
                        case EXIT_CURRENT_GAME:
                            return;
                        default:
                            throw new SnakesAndLaddersRunTimeException("runSingleGame(): Invalid gameOption input.");
                    }
                }
            }
        }

        if (hasGameWon()) {
            aPlayer winnerPlayer = m_gameModel.getWinnerPlayer(m_NumOfSoldiersToWin);
            m_consoleView.displayWinner(winnerPlayer.getPlayerName());
        } else {
            m_consoleView.displayNoWinner();
        }
    }

    public List<aPlayer> getPlayers() {
        return m_gameModel.getPlayers();
    }

    private void createNewGame() throws SnakesAndLaddersRunTimeException {
        try {
            int boardSize = m_consoleView.GetBoardSize();
            int numOfSnakesAndLadders = m_consoleView.getNumOfSnakesAndLadders(boardSize);
            int numOfPlayers = m_consoleView.GetNumOfPlayers();
            m_NumOfSoldiersToWin = m_consoleView.GetNumOfSoldiersToWin();
            m_gameModel = new GameModel(boardSize, numOfSnakesAndLadders, numOfPlayers);
        } catch (SnakesAndLaddersRunTimeException ex) {
            m_consoleView.printSnakesAndLaddersRunTimeExceptiom(ex);
        }

        m_gameModel.initNewGame();
        initPlayers();
        m_gameModel.selectFirstPlayer();
    }

    private eStartMenu startLoadGame() throws SnakesAndLaddersRunTimeException {
        eXMLLoadStatus status = loadGame();

        if (status != eXMLLoadStatus.LOAD_SUCCESS) {
            return eStartMenu.CHOOSE;
        }

        runGame();
        return eStartMenu.EXIT;
    }

    private eXMLLoadStatus loadGame() throws SnakesAndLaddersRunTimeException {
        eXMLLoadStatus loadStatus;

        //Noam: "First Init Table from XML
        GameModel modelLoad = null;
        String xmlPath = m_consoleView.getLoadXMLPath();
        loadStatus = XML.initModelFromXml(xmlPath, modelLoad, m_NumOfSoldiersToWin);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            m_consoleView.displayXMLLoadError(loadStatus);
            return loadStatus;
        }
        modelLoad = new GameModel(XML.getM_GameSize(), XML.getM_NumOfSnakesAndLadders(), XML.getM_NumOfPlayers());
        loadStatus = XML.loadXML(xmlPath, modelLoad, m_NumOfSoldiersToWin);

        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            m_consoleView.displayXMLLoadError(loadStatus);
            return loadStatus;
        }

        m_gameModel = modelLoad;

        return loadStatus;
    }

    private void makeMove() throws SnakesAndLaddersRunTimeException {
        BoardSquare move;
        //aPlayer player = m_gameModel.getCurrPlayer();

        move = selectSquareToFill();
        m_gameModel.GetSingleGame().setCurrentBoardSquare(move);

        //m_consoleView.displayLastMove(player, move); - Noam: "Check if needed"
        m_consoleView.printGame(GetSingleGame(), getPlayers());
    }

    private void saveGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void saveGameAs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initPlayers() throws SnakesAndLaddersRunTimeException {
        aPlayer player;
        ePlayerType playertype;
        String playerName;
        int playerNumOfSoldiersToWin;

        for (int i = 0; i < m_gameModel.getNumOfPlayers(); i++) {
            playertype = m_consoleView.getPlayerType(i);

            switch (playertype) {
                case Human:
                    playerName = m_consoleView.getPlayerString();
                    player = new HumanPlayer(playerName);
                    player.initSoldiers(m_gameModel.getCurrGameIndex());
                    m_gameModel.addPlayer(player);
                    break;
                case Computer:
                    player = new ComputerPlayer("Computer");
                    player.initSoldiers(m_gameModel.getCurrGameIndex());
                    m_gameModel.addPlayer(player);
                    break;
                default:
                    throw new SnakesAndLaddersRunTimeException("InitPlayers(): Invalid PlayerType Input");
            }
        }
    }

    public SnakesAndLaddersSingleGame GetSingleGame() {
        return m_gameModel.GetSingleGame();
    }

    private BoardSquare selectSquareToFill() throws SnakesAndLaddersRunTimeException {
        BoardSquare nextMove;
        int cubeAnswer;
        Cube cube = new Cube();
        aPlayer player = m_gameModel.getCurrPlayer();

        if (player instanceof ComputerPlayer) {
            m_consoleView.LetComputerPlay();
            System.out.println(player);
            int soldierIndex = player.randomizeCurrentPlayer();
            m_consoleView.printCurrentSoldier(soldierIndex);
        } else {
            int indexOfSoldier = m_consoleView.GetSoldierToPlayWith(player);
            player.setCurrentSoldier(indexOfSoldier);
            m_consoleView.ThrowCube();
        }
        cubeAnswer = cube.throwCube();
        m_consoleView.PrintCubeAnswer(cubeAnswer);
        nextMove = move(player, cubeAnswer);

        return nextMove;
    }

    private BoardSquare move(aPlayer player, int cubeAnswer) throws SnakesAndLaddersRunTimeException {
        BoardSquare boardToMove;
        Soldier currentSoldier = player.GetCurrentSoldier();
        int oldPlyerIndex = currentSoldier.getLocationOnBoard().getSquareNumber();
        int newPlayerIndex = oldPlyerIndex + cubeAnswer;

        if (newPlayerIndex < (m_gameModel.GetSingleGame().getMAX_SQUARE_NUM())) {
            boardToMove = m_gameModel.GetSingleGame().getBoardSquare(newPlayerIndex);
            currentSoldier.setLocationOnBoard(boardToMove);
        }
        else{
            boardToMove = m_gameModel.GetSingleGame().getBoardSquare( m_gameModel.GetSingleGame().getO_BoardSize()-1);
            currentSoldier.setLocationOnBoard(boardToMove);
        }

        switch (boardToMove.getType()) {
            case LADDER_TAIL:
                boardToMove = boardToMove.getJumpTo();
                break;
            case SNAKE_HEAD:
                boardToMove = boardToMove.getJumpTo();
                break;
            case NONE:
                break;
        }
        m_gameModel.setMove(player, boardToMove);
        //player.ForwardCurrentSoldier(); - Noam: "Now get current soldier from user"
        return boardToMove;
    }

    public int getNumOfSoldiersToWin() {
        return m_NumOfSoldiersToWin;
    }

    public void setNumOfSoldiersToWin(int m_NumOfSoldiersToWin) {
        this.m_NumOfSoldiersToWin = m_NumOfSoldiersToWin;
    }

    //TODO fixit
    public boolean hasGameWon() {
        boolean returnValue = false;
        for (aPlayer player : getPlayers()) {
            if (getNumOfSoldiersToWin() == 0) {
                returnValue = true;
            }
        }
        return false;
    }
}
