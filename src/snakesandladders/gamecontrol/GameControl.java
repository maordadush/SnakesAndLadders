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
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.ePlayerType;
import static snakesandladders.players.ePlayerType.COMPUTER;
import snakesandladders.xml.XML;
import snakesandladders.xml.eXMLLoadStatus;
import snakesandladders.xml.eXMLSaveStatus;

/**
 *
 * @author Noam
 */
public class GameControl {

    private ConsoleView m_consoleView;
    private GameModel m_gameModel;

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
                        m_gameModel.GetSingleGame().shuffleSnakesAndLadders(m_gameModel.getM_NumOfSoldiersToWin());
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
        SinglePlayer player;
        //BoardSquare currGameIndex;

        while (!hasGameWon()) {
            //currGameIndex = m_gameModel.getCurrGameIndex();
            player = m_gameModel.getCurrPlayer();

            m_consoleView.ClearScreen();
            m_consoleView.printGameName(m_gameModel.getM_GameName());
            m_consoleView.displayCurrPlayer(player);
            m_consoleView.printGame(GetSingleGame(), getPlayers());

            gameOption = eGameMenu.CHOOSE;
            while (gameOption == eGameMenu.CHOOSE) {
                gameOption = m_consoleView.getGameOption();
                switch (gameOption) {
                    case MAKE_MOVE:
                        makeMove();
                        m_gameModel.forwardPlayer();
                        break;
                    case SAVE_GAME:
                        saveGame();
                        gameOption = eGameMenu.CHOOSE;
                        break;
                    case SAVE_GAME_AS:
                        saveGameAs();
                        gameOption = eGameMenu.CHOOSE;
                        break;
                    case EXIT_CURRENT_GAME:
                        return;
                    default:
                        throw new SnakesAndLaddersRunTimeException("runSingleGame(): Invalid gameOption input.");
                }
            }
        }

        if (hasGameWon()) {
            SinglePlayer winnerPlayer = m_gameModel.getWinnerPlayer(m_gameModel.getM_NumOfSoldiersToWin());
            m_consoleView.displayWinner(winnerPlayer.getPlayerName());
        } else {
            m_consoleView.displayNoWinner();
        }

    }

    public List<SinglePlayer> getPlayers() {
        return m_gameModel.getPlayers();
    }

    private void createNewGame() throws SnakesAndLaddersRunTimeException {
        try {
            int boardSize = m_consoleView.GetBoardSize();
            int numOfSnakesAndLadders = m_consoleView.getNumOfSnakesAndLadders(boardSize);
            int numOfPlayers = m_consoleView.GetNumOfPlayers();
            int numOfSoldiersToWin = m_consoleView.GetNumOfSoldiersToWin();
            m_gameModel = new GameModel(boardSize, numOfSnakesAndLadders, numOfPlayers, numOfSoldiersToWin);
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
        loadStatus = XML.initModelFromXml(xmlPath, modelLoad);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            m_consoleView.displayXMLLoadError(loadStatus);
            return loadStatus;
        }
        modelLoad = new GameModel(XML.getM_GameSize(), XML.getM_NumOfSnakesAndLadders(), XML.getM_NumOfPlayers(), XML.getM_NumOfSoldiersToWin());
        loadStatus = XML.loadXML(xmlPath, modelLoad);

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
        if (m_gameModel.getSaveGamePath() == null) {
            saveGameAs();
        } else {
            eXMLSaveStatus saveStatus;
            saveStatus = XML.saveXML(m_gameModel.getSaveGamePath(), m_gameModel);
            if (saveStatus != eXMLSaveStatus.SAVE_SUCCESS) {
                m_consoleView.displayXMLSaveError(saveStatus);
            } else {
                m_consoleView.displayXMLSavedSuccessfully(m_gameModel.getSaveGamePath());
            }
        }

    }

    private void saveGameAs() {
        String savePath = m_consoleView.getSaveXMLPath();
        eXMLSaveStatus saveStatus;
        saveStatus = XML.saveXML(savePath, m_gameModel);

        if (saveStatus == eXMLSaveStatus.SAVE_SUCCESS) {
            m_gameModel.setSaveGamePath(savePath);
            m_consoleView.displayXMLSavedSuccessfully(savePath);
        } else {
            m_consoleView.displayXMLSaveError(saveStatus);
        }
    }

    private void initPlayers() throws SnakesAndLaddersRunTimeException {
        SinglePlayer player;
        ePlayerType playertype;
        String playerName;
        int playerNumOfSoldiersToWin;
        int computerIndex = 0;

        for (int i = 0; i < m_gameModel.getNumOfPlayers(); i++) {
            playertype = m_consoleView.getPlayerType(i);

            switch (playertype) {
                case HUMAN:
                    playerName = m_consoleView.getPlayerString(m_gameModel.getPlayers());
                    player = new SinglePlayer(playerName, playertype);
                    player.initSoldiers(m_gameModel.getCurrGameIndex());
                    m_gameModel.getCurrGameIndex().getPlayers().add(player);
                    m_gameModel.addPlayer(player);
                    break;
                case COMPUTER:
                    player = new SinglePlayer("Computer" + computerIndex, playertype);
                    player.initSoldiers(m_gameModel.getCurrGameIndex());
                    m_gameModel.getCurrGameIndex().getPlayers().add(player);
                    m_gameModel.addPlayer(player);
                    computerIndex++;
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
        SinglePlayer player = m_gameModel.getCurrPlayer();

        if (player.getType() == COMPUTER) {
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

    private BoardSquare move(SinglePlayer player, int cubeAnswer) throws SnakesAndLaddersRunTimeException {
        BoardSquare boardToMove;
        Soldier currentSoldier = player.GetCurrentSoldier();
        int oldPlyerIndex = currentSoldier.getLocationOnBoard().getSquareNumber();
        int newPlayerIndex = oldPlyerIndex + cubeAnswer;

        if (newPlayerIndex < (m_gameModel.GetSingleGame().getMAX_SQUARE_NUM())) {
            boardToMove = m_gameModel.GetSingleGame().getBoardSquare(newPlayerIndex);
            currentSoldier.setLocationOnBoard(boardToMove);
        } else {
            boardToMove = m_gameModel.GetSingleGame().getBoardSquare(m_gameModel.GetSingleGame().getMAX_SQUARE_NUM());
            currentSoldier.setLocationOnBoard(boardToMove);
            currentSoldier.setM_FinishedGame(true);
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

    //TODO fixit
    public boolean hasGameWon() {

        int winningCount = 0;
        for (SinglePlayer player : getPlayers()) {
            for (Soldier soldier : player.getM_SoldiersList()) {
                if (soldier.isM_FinishedGame()) {
                    winningCount++;
                }
                if (winningCount == m_gameModel.getM_NumOfSoldiersToWin()) {
                    return true;
                }
            }
            winningCount = 0;
        }
        return false;
    }
}
