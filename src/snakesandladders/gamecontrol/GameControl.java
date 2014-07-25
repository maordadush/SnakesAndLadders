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
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;
import snakesandladders.players.ComputerPlayer;
import snakesandladders.players.HumanPlayer;
import snakesandladders.players.aPlayer;
import snakesandladders.players.ePlayerType;

/**
 *
 * @author Noam
 */
public class GameControl {

    private ConsoleView m_consoleView;
    private GameModel m_gameModel;

    public GameControl() {
        m_consoleView = new ConsoleView();
        try {
            int boardSize = m_consoleView.GetBoardSize();
            int numOfPlayers = m_consoleView.GetNumOfPlayers();
            m_gameModel = new GameModel(boardSize, numOfPlayers);
        } catch (SnakesAndLaddersRunTimeException ex) {
            m_consoleView.printSnakesAndLaddersRunTimeExceptiom(ex);
        }
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
                        //              mainOpt = startLoadGame();
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
        //XMLLoadStatus loadStatus;

        while (endGameOption != eEndMenu.EXIT_GAME) {
            runSingleGame();
            endGameOption = eEndMenu.CHOOSE;

            while (endGameOption == eEndMenu.CHOOSE) {
                endGameOption = m_consoleView.getEndGameOption();
                switch (endGameOption) {
                    case RESTART_GAME:
                        m_gameModel.initGame();
                        break;
                    case START_NEW_GAME:
                        createNewGame();
                        break;
                    case LOAD_GAME:
//                        loadStatus = loadGame();
//                        if (loadStatus != XMLLoadStatus.LOAD_SUCCESS) {
//                            endGameOption = eEndMenu.CHOOSE;
//                        }
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
        BoardSquare currGameIndex;

        while (!m_gameModel.hasGameWon()) {
            currGameIndex = m_gameModel.getCurrGameIndex(); //dadush start from initialize currentIndex
            player = m_gameModel.getCurrPlayer();
            m_consoleView.displayCurrPlayerAndGameIndex(currGameIndex, player, m_gameModel.GetSelectNextGame());
            m_consoleView.printGame(GetSingleGame(), getPlayers());
            if (player instanceof ComputerPlayer) {
                makeMove();
            } else {
                gameOption = eGameMenu.CHOOSE;
                while (gameOption == eGameMenu.CHOOSE) {
                    gameOption = m_consoleView.getGameOption();
                    switch (gameOption) {
                        case MAKE_MOVE:
                            makeMove();
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

        if (m_gameModel.hasGameWon()) {
            aPlayer winnerPlayer = m_gameModel.getWinnerPlayer();
            m_consoleView.displayWinner(winnerPlayer.getPlayerName());
        } else {
            m_consoleView.displayNoWinner();
        }
    }

    public List<aPlayer> getPlayers() {
        return m_gameModel.getPlayers();
    }

    private void createNewGame() throws SnakesAndLaddersRunTimeException {
        m_gameModel.initNewGame();
        initPlayers();
        m_gameModel.selectFirstPlayer();
    }

//   private eStartMenu startLoadGame() throws SnakesAndLaddersRunTimeException {
//        XMLLoadStatus status = loadGame();
//
//        if (status != XMLLoadStatus.LOAD_SUCCESS) {
//            return eStartMenu.CHOOSE;
//        }
//
//      runGame();
//        return eStartMenu.EXIT;
//    }
    private void makeMove() throws SnakesAndLaddersRunTimeException {
        BoardSquare move;
        aPlayer player = m_gameModel.getCurrPlayer();

        move = selectSquareToFill();
        m_gameModel.makeMove(move);
        m_consoleView.displayLastMove(player, move);
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
                    playerNumOfSoldiersToWin = m_consoleView.GetNumOfSoldiersToWin();
                    player = new HumanPlayer(playerName, m_gameModel.NUM_OF_SOLDIERS, playerNumOfSoldiersToWin);
                    m_gameModel.addPlayer(player);
                    break;
                case Computer:
                    playerNumOfSoldiersToWin = m_consoleView.GetNumOfSoldiersToWin();
                    player = new ComputerPlayer("Computer", m_gameModel.NUM_OF_SOLDIERS, playerNumOfSoldiersToWin);
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
        aPlayer player = m_gameModel.getCurrPlayer();

        if (player instanceof ComputerPlayer) {
            cubeAnswer = m_consoleView.GetAutomaticCubeAnswer();
        } else {
            cubeAnswer = m_consoleView.GetCubeAnswer();
        }

        nextMove = move(player, cubeAnswer);

        return nextMove;
    }

    private BoardSquare move(aPlayer player, int cubeAnswer) throws SnakesAndLaddersRunTimeException {
        BoardSquare boardToMove;
        int xToMove = (int) player.getCurrentSoldier().getLocationOnBoard().getX() + cubeAnswer;
        int yToMove = (int) player.getCurrentSoldier().getLocationOnBoard().getY();
        int boardSize = GetSingleGame().getO_BoardSize() - 1;

        if (xToMove < boardSize) {
            boardToMove = GetSingleGame().getBoardSquare(xToMove, yToMove);
        } else {
            yToMove += 1;
            xToMove = boardSize - xToMove;
            boardToMove = GetSingleGame().getBoardSquare(xToMove, yToMove);
        }
        if ((xToMove >= boardSize) && (yToMove >= boardSize)) {
            boardToMove = GetSingleGame().getBoardSquare(boardSize, boardSize);
        }

        if (boardToMove == null) {
            throw new SnakesAndLaddersRunTimeException("move(): Error board move.");
        }

        switch (boardToMove.getType()) {
            case LADDER_HEAD:
                boardToMove = boardToMove.getJumpTo();
                break;
            case SNAKE_HEAD:
                boardToMove = boardToMove.getJumpTo();
                break;
            case NONE:
                break;
        }
        return boardToMove;
    }
}
