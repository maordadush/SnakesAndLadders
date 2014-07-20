/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamecontrol;

import snakesandladders.consoleview.ConsoleView;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.players.ComputerPlayer;
import snakesandladders.players.aPlayer;

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
                        mainOpt = startLoadGame();
                        break;
                    case EXIT:
                        break;
                    default:
                        throw new SnakesAndLaddersRunTimeException("RunXmixDrixNextGeneration(): Invalid mainOpt input.");
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
            currGameIndex = m_gameModel.getCurrGameIndex();
            player = m_gameModel.getCurrPlayer();
            //Noam - Stopped here!
            m_consoleView.displayCurrPlayerAndGameIndex(currGameIndex, player, m_gameModel.selectNextGame());
            m_consoleView.printGame(m_gameModel.getGame());
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
                            throw new SnakesAndLaddersRunTimeException("RunGame(): Invalid gameOption input.");
                    }
                }
            }
        }

        if (model.hasGameWon()) {
            XMixDrixPlayer winnerPlayer = model.getWinnerPlayer();
            view.displayWinner(winnerPlayer.getPlayerName(), winnerPlayer.getPlayerChar());
        } else {
            view.displayNoWinner();
        }
    }

    private void createNewGame() throws SnakesAndLaddersRunTimeException {
        m_gameModel.initNewGame();
        initPlayers();
        m_gameModel.selectFirstPlayer();
    }
}
