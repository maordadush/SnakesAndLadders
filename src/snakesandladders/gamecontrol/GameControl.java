/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamecontrol;

import java.util.ArrayList;
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

    private final ConsoleView m_consoleView;
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
                        restartGame();
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
        player = m_gameModel.getCurrPlayer();
        m_consoleView.displayCurrPlayer(player);

        m_consoleView.ClearScreen();

        while (!hasGameWon()) {
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
        boolean shuffleLaddersAndSnakes = true;
        try {
            SinglePlayer.setNextId(0);
            int boardSize = m_consoleView.GetBoardSize();
            int numOfSnakesAndLadders = m_consoleView.getNumOfSnakesAndLadders(boardSize);
            int numOfPlayers = m_consoleView.GetNumOfPlayers();
            int numOfSoldiersToWin = m_consoleView.GetNumOfSoldiersToWin();

            m_gameModel = new GameModel(boardSize, numOfSnakesAndLadders, numOfPlayers, numOfSoldiersToWin);
        } catch (SnakesAndLaddersRunTimeException ex) {
            m_consoleView.printSnakesAndLaddersRunTimeExceptiom(ex);
        }

        m_gameModel.initNewGame(shuffleLaddersAndSnakes);
        initPlayers(null);
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
        SinglePlayer.setNextId(0);
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

        m_consoleView.printGame(GetSingleGame(), getPlayers());

        move = selectSquareToFill();
        m_gameModel.getGame().setCurrentBoardSquare(move);

        m_consoleView.waitForEnter();
        m_consoleView.ClearScreen();
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

    private void initPlayers(List<SinglePlayer> players) throws SnakesAndLaddersRunTimeException {
        SinglePlayer player;
        ePlayerType playertype;
        String playerName;
        int computerIndex = 0;

        for (int i = 0; i < m_gameModel.getNumOfPlayers(); i++) {
            if (players == null) {
                playertype = m_consoleView.getPlayerType(i);
            } else {
                playertype = players.get(i).getType();
            }
            switch (playertype) {
                case HUMAN:
                    playerName = players == null ? m_consoleView.getPlayerString(m_gameModel.getPlayers())
                            : players.get(i).getPlayerName();
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
        return m_gameModel.getGame();
    }

    private BoardSquare selectSquareToFill() throws SnakesAndLaddersRunTimeException {
        BoardSquare nextMove;
        int cubeAnswer;
        Cube cube = new Cube();
        SinglePlayer player = m_gameModel.getCurrPlayer();

        cubeAnswer = cube.throwCube();

        if (player.getType() == COMPUTER) {
            m_consoleView.LetComputerPlay();
            m_consoleView.PrintCubeAnswer(cubeAnswer);
            m_consoleView.PrintPlayer(player);
            int soldierIndex = player.randomizeCurrentPlayer();
            m_consoleView.printCurrentSoldier(soldierIndex);
        } else {
            m_consoleView.PrintCubeAnswer(cubeAnswer);
            int indexOfSoldier = m_consoleView.GetSoldierToPlayWith(player);
            player.setCurrentSoldier(indexOfSoldier);
        }

        nextMove = move(player, cubeAnswer);

        return nextMove;
    }

    private BoardSquare move(SinglePlayer player, int cubeAnswer) throws SnakesAndLaddersRunTimeException {
        Soldier currentSoldier = player.GetCurrentSoldier();
        BoardSquare originSquare = currentSoldier.getLocationOnBoard();
        BoardSquare boardToMove;
        int oldPlyerIndex = originSquare.getSquareNumber();
        int newPlayerIndex = oldPlyerIndex + cubeAnswer;

        if (newPlayerIndex < (m_gameModel.getGame().getMAX_SQUARE_NUM())) {
            boardToMove = m_gameModel.getGame().getBoardSquare(newPlayerIndex);
        } else {
            boardToMove = m_gameModel.getGame().getBoardSquare(m_gameModel.getGame().getMAX_SQUARE_NUM());
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

        boardToMove.getPlayers().add(player);
        //check if exist soldiers in origin square
        if (!player.atSquare(originSquare)) {
            originSquare.getPlayers().remove(player);
        }
        m_gameModel.setMove(player, boardToMove);
        return boardToMove;
    }

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

    private void restartGame() throws SnakesAndLaddersRunTimeException {
        boolean shuffleLaddersAndSnakes = false;
        List<BoardSquare> currentChars = m_gameModel.getSnakesAndLaddersSquares();
        List<SinglePlayer> currPlayers = new ArrayList<>(m_gameModel.getPlayers());

        SinglePlayer.setNextId(0);
        m_gameModel.initNewGame(shuffleLaddersAndSnakes);
        initPlayers(currPlayers);
        for (BoardSquare boardSquare : currentChars) {
            BoardSquare newSquare = m_gameModel.getGame().getBoardSquare(boardSquare.getSquareNumber());
            newSquare.setType(boardSquare.getType());
            newSquare.setJumpTo(boardSquare.getJumpTo());
        }
        m_gameModel.selectFirstPlayer();
    }
}
