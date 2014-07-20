/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.consoleview;

import java.util.Scanner;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamecontrol.eEndMenu;
import snakesandladders.gamecontrol.eGameMenu;
import snakesandladders.gamecontrol.eStartMenu;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;
import snakesandladders.players.aPlayer;
import snakesandladders.players.ePlayerType;

/**
 *
 * @author Noam
 */
public class ConsoleView {

    private void showMainMenu() {
        System.out.println("Please Enter Your Selection:");
        System.out.println("1. Create New Game.");
        System.out.println("2. Load Game.");
        System.out.println("3. Exit Game.");
    }

    public int GetBoardSize()throws SnakesAndLaddersRunTimeException{
        Scanner scanner = new Scanner(System.in);
        int input;
        
        showMenuSize();
        
        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }
        input = scanner.nextInt();
        
        while (input < 5 || input > 8) {
            System.out.println("Not Valid input, Please enter again:");
            showMenuSize();

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }
        
        return input;
    }
            
    public eStartMenu GetMainOptionMenu() throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int input;

        showMainMenu();

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }

        input = scanner.nextInt();

        while (input < 1 || input > 3) {
            System.out.println("Not Valid input, Please enter again:");
            showMainMenu();

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }

        switch (input) {
            case 1:
                return eStartMenu.START_NEW_GAME;
            case 2:
                return eStartMenu.LOAD_GAME;
            case 3:
                return eStartMenu.EXIT;
            default:
                throw new SnakesAndLaddersRunTimeException("GetMainOptionMenu(): Invalid input.");
        }
    }

    public ePlayerType getPlayerType(int playerNumber) throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int type;

        System.out.println("Enter Player " + playerNumber + " Type:");
        System.out.println("1. Human.");
        System.out.println("2. Computer.");

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }

        type = scanner.nextInt();

        while (type != 1 && type != 2) {
            System.out.println("Not Valid input, Please enter again:");
            System.out.println("Enter Player " + playerNumber + " Type:");
            System.out.println("1. Human.");
            System.out.println("2. Computer.");

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            type = scanner.nextInt();
        }

        switch (type) {
            case 1:
                return ePlayerType.Human;
            case 2:
                return ePlayerType.Computer;
            default:
                throw new SnakesAndLaddersRunTimeException("GetPlayerType(): Invalid PlayerType input");
        }
    }

    public String getPlayerString() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Player Name.");
        return scanner.next();
    }

//    public SquareIndex getSquareInput() {
//        Scanner scanner = new Scanner(System.in);
//        SquareIndex index;
//        int x;
//        int y;
//
//        while (!scanner.hasNextInt()) {
//            System.out.println("Inavlid Input. Please enter a number.");
//            scanner.next();
//        }
//
//        x = scanner.nextInt();
//
//        while (!scanner.hasNextInt()) {
//            System.out.println("Inavlid Input. Please enter a number.");
//            scanner.next();
//        }
//
//        y = scanner.nextInt();
//
//        index = new SquareIndex(x, y);
//
//        return index;
//    }
//    public SquareIndex getNextMove() {
//        SquareIndex move;
//
//        System.out.println("Enter where you want to make a move: x y (x/y between 0-"
//                + (XMixDrixSingleGame.BOARD_SIZE - 1) + ")");
//
//        move = getSquareInput();
//        while (!XMixDrixSingleGame.isSquareIndexValid(move)) {
//            System.out.println("Inavlid move input, please enter again.");
//            move = getSquareInput();
//        }
//
//        return move;
//    }
//    public SquareIndex getGameIndex() {
//        SquareIndex gameIndex;
//
//        System.out.println("Choose a game for next move: x y (x/y between 0-"
//                + (XMixDrixSingleGame.BOARD_SIZE - 1) + ")");
//
//        gameIndex = getSquareInput();
//        while (!XMixDrixSingleGame.isSquareIndexValid(gameIndex)) {
//            System.out.println("Inavlid gameIndex input, please enter again.");
//            gameIndex = getSquareInput();
//        }
//
//        return gameIndex;
//    }
    private void showGameOption() {
        System.out.println("Please Enter Your Selection:");
        System.out.println("1. Make a move.");
        System.out.println("2. Save Game.");
        System.out.println("3. Save Game As.");
        System.out.println("4. Exit Current Game.");
    }

    public eGameMenu getGameOption() throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int input;

        showGameOption();

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }

        input = scanner.nextInt();

        while (input < 1 || input > 4) {
            System.out.println("Not Valid input, Please enter again:");
            showGameOption();

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }

        switch (input) {
            case 1:
                return eGameMenu.MAKE_MOVE;
            case 2:
                return eGameMenu.SAVE_GAME;
            case 3:
                return eGameMenu.SAVE_GAME_AS;
            case 4:
                return eGameMenu.EXIT_CURRENT_GAME;
            default:
                throw new SnakesAndLaddersRunTimeException("GetGameOption(): Invalid input.");
        }
    }

    private void showEndGameOption() {
        System.out.println("Please Enter Your Selection:");
        System.out.println("1. RestartGame.");
        System.out.println("2. New Game.");
        System.out.println("3. Load Game.");
        System.out.println("4. Exit.");
    }

    public eEndMenu getEndGameOption() throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int input;

        showEndGameOption();

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }

        input = scanner.nextInt();

        while (input < 1 || input > 4) {
            System.out.println("Not Valid input, Please enter again:");
            showEndGameOption();

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }

        switch (input) {
            case 1:
                return eEndMenu.RESTART_GAME;
            case 2:
                return eEndMenu.START_NEW_GAME;
            case 3:
                return eEndMenu.LOAD_GAME;
            case 4:
                return eEndMenu.EXIT_GAME;
            default:
                throw new SnakesAndLaddersRunTimeException("GetEndGameOption(): Invalid input.");
        }
    }

//    public void printChar(XMixDrixChars printChar) throws XMixDrixRunTimeException {
//        switch (printChar) {
//            case X:
//                System.out.print("x");
//                break;
//            case O:
//                System.out.print("o");
//                break;
//            case X_WIN:
//                System.out.print("X");
//                break;
//            case O_WIN:
//                System.out.print("O");
//                break;
//            case NONE:
//                System.out.print(" ");
//                break;
//            default:
//                throw new XMixDrixRunTimeException("PrintChar(): Not valid char input.");
//        }
//    }
    public void printGame(SnakesAndLaddersSingleGame game) throws SnakesAndLaddersRunTimeException {
//        XMixDrixChars printChar;
//        int singleGameBoardSize = XMixDrixSingleGame.BOARD_SIZE;
//        int ngBoardSize = XMixDrixSingleGame.BOARD_SIZE * XMixDrixSingleGame.BOARD_SIZE;
//        SquareIndex gameIndex = new SquareIndex(0, 0);
//        SquareIndex index = new SquareIndex(0, 0);
//
//        for (int i = 0; i < ngBoardSize; i++) {
//            for (int j = 0; j < ngBoardSize; j++) {
//                gameIndex.setSquare(i / singleGameBoardSize, j / singleGameBoardSize);
//                index.setSquare(i % singleGameBoardSize, j % singleGameBoardSize);
//                printChar = game.getCharFromGame(gameIndex, index);
//                printChar(printChar);
//                if (j != 8) {
//                    if (j % singleGameBoardSize == 2) {
//                        System.out.print(" # ");
//                    } else {
//                        System.out.print(" | ");
//                    }
//                } else {
//                    System.out.print('\n');
//                }
//            }
//            if (i != 8) {
//                if (i % singleGameBoardSize == 2) {
//                    System.out.println("#################################");
//                } else {
//                    System.out.println(" -------  #  -------  #  ------- ");
//                }
//            }
//        }
//        System.out.println("");
    }

    public void displayCurrPlayerAndGameIndex(BoardSquare index, aPlayer player, boolean selectNextGame, boolean isCurrGameFull) {
        int x = (int) index.getX();
        int y = (int) index.getY();

        System.out.print(player.getPlayerName());
        System.out.println("Your current index is:" + "(" + x + "," + y + ")");

        System.out.println("");
    }

    public void displayWinner(String name) {
        System.out.println("The winner is: " + name);
    }

//    public void displayNoWinner() {
//        System.out.println("The game is Tie");
//    }

//    public void displayLastMove(XMixDrixPlayer player, SquareIndex move, SquareIndex gameIndex) {
//        System.out.println(player.getPlayerName() + " last move: " + move.getX() + " " + move.getY()
//                + ", in game: " + gameIndex.getX() + " " + gameIndex.getY());
//    }

    public void printSnakesAndLaddersRunTimeExceptiom(SnakesAndLaddersRunTimeException ex) {
        System.out.println(ex.toString() + ": \n" + ex.getMessage());
    }

//    public void displayXMLLoadErro(XMLLoadStatus loadStatus) {
//        System.out.println("Error loading game from XML: " + loadStatus.toString());
//    }

    public String getLoadXMLPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter full XML path to load game:");

        return scanner.next();
    }

    public String getSaveXMLPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter full XML path to save game:");

        return scanner.next();
    }

//    public void displayXMLSaveError(XMLSaveStatus saveStatus) {
//        System.out.println("Error saving game to XML: " + saveStatus.toString());
//    }

    public void displayXMLSavedSuccessfully(String savePath) {
        System.out.println("Game SaveSuccessfully to: " + savePath);
    }

    private void showMenuSize() {
        System.out.println("Welcome to Snakes and ladders game");
        System.out.println("First, please write the board size (5-8):");
    }

    public int GetNumOfPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
