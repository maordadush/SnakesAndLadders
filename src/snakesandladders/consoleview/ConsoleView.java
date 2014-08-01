/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.consoleview;

import java.util.List;
import java.util.Scanner;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamecontrol.eEndMenu;
import snakesandladders.gamecontrol.eGameMenu;
import snakesandladders.gamecontrol.eStartMenu;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;
import snakesandladders.gamemodel.eChars;
import snakesandladders.players.Soldier;
import snakesandladders.players.aPlayer;
import snakesandladders.players.ePlayerType;
import snakesandladders.xml.eXMLLoadStatus;

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

    public int GetBoardSize() throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int input;

        showMenuBoardSize();

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }
        input = scanner.nextInt();

        while (input < 5 || input > 8) {
            System.out.println("Not Valid input, Please enter again:");
            showMenuBoardSize();

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }

        return input;
    }

    public int getNumOfSnakesAndLadders(int boardSize) throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int input;

        showMenuSnakesAndLaddersSize();

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }
        input = scanner.nextInt();
        // TODO: Check Verified snakes And Ladders
        while (input < 1 || input > (boardSize * boardSize) - 2) {
            System.out.println("Not Valid input, Please enter again:");
            showMenuSnakesAndLaddersSize();

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }

        return input;
    }

    public int GetNumOfSoldiersToWin() throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int input;

        showNumOfSoldiers();

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }
        input = scanner.nextInt();

        while (input < 1 || input > 4) {
            System.out.println("Not Valid input, Please enter again:");
            showNumOfSoldiers();

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
    public void printGame(SnakesAndLaddersSingleGame o_Game, List<aPlayer> o_Players) throws SnakesAndLaddersRunTimeException {
        int singleGameBoardSize = o_Game.getO_BoardSize();
        List<aPlayer> players = o_Players;
        int numPlayers = players.size();
        StringBuilder boardString = new StringBuilder();

        for (int i = singleGameBoardSize - 1; i >= 0; i--) {
            for (int j = 0; j < singleGameBoardSize; j++) {
                BoardSquare bs = o_Game.getBoardSquare(i, j);
                eChars bsType = bs.getType();
                boardString.append(String.format("%02d", bs.getSquareNumber())).append("|");
                if (bsType == eChars.LADDER_TAIL || bsType == eChars.SNAKE_HEAD) {
                    boardString.append(String.format("%02d", bs.getJumpTo().getSquareNumber())).append("|"); //+ bsType + "|");
                } else {
                    boardString.append("--|"); //+ bsType + "|");
                }
                for (int k = 0; k < numPlayers; k++) {
                    boardString.append(players.get(k).getNumSoldiersAtSquare(bs));
                }
                boardString.append(" ");
            }
            boardString.append(System.lineSeparator());
        }
        boardString.append("--------------------------------------------------------------").append(System.lineSeparator());
        for (aPlayer player : players) {
            boardString.append("Player " + (players.indexOf(player) + 1) + ": " + player.getPlayerName() + "\t");
        }
        boardString.append(System.lineSeparator());

        //Print out the board
        System.out.print(boardString.toString());
    }

//    //Noam - Check ablut selectNextGame
//    public void displayCurrPlayerAndGameIndex(BoardSquare index, aPlayer player) {
//        int x = (int) index.getX();
//        int y = (int) index.getY();
//
//        System.out.print(player.getPlayerName());
//        System.out.println(" your current index is:" + "(" + x + "," + y + ")");
//
//        System.out.println();
//    }
    public void displayWinner(String name) {
        System.out.println("The winner is: " + name);
    }

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

    private void showMenuBoardSize() {
        System.out.println("Welcome to Snakes and ladders game");
        System.out.println("First, please write the board size (5-8):");
    }

    public int GetNumOfPlayers() throws SnakesAndLaddersRunTimeException {
        Scanner scanner = new Scanner(System.in);
        int input;

        showNumOfPlayersMenu();

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }

        input = scanner.nextInt();

        while (input < 2 || input > 4) {
            System.out.println("Not Valid input, Please enter again:");
            showNumOfPlayersMenu();

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }
        return input;
    }

    public void showNumOfPlayersMenu() {
        System.out.println("How many players (2-4)? ");
    }

    public void showNumOfSoldiers() {
        System.out.println("How many soldiers from 4 need to finish for win (1-4)?");

    }

    public void displayNoWinner() {
        System.out.println("The game is Tie");
    }

    public void displayLastMove(aPlayer player, BoardSquare move) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void ThrowCube() {
        System.out.println("Press 'Enter' to throw the cube");
        Scanner reader = new Scanner(System.in);
        String str = reader.nextLine();
    }

    public void LetComputerPlay() {
        System.out.println("Press 'Enter' to see computer turn");
        Scanner reader = new Scanner(System.in);
        String str = reader.nextLine();
    }

    private void showMenuSnakesAndLaddersSize() {
        System.out.println("Second, please write the number of snakes and ladders:");
    }

    public int GetSoldierToPlayWith(aPlayer player) {
        Scanner scanner = new Scanner(System.in);
        int input;

        displaySoldiersOfPlayer(player);
        System.out.println("Choose soldier (1-4 that now finished game):");

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }

        input = scanner.nextInt();

        while ((input < 1 || input > 4) && (player.getM_SoldiersList()[input-1].isM_FinishedGame())) {
            System.out.println("Not Valid input, Please enter again:");
            displaySoldiersOfPlayer(player);
            System.out.println("Choose soldier (1-4 that now finished game):");

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }

        return input;
    }

    public void displaySoldiersOfPlayer(aPlayer player) {
        Soldier[] soldierList = player.getM_SoldiersList();
        StringBuilder playerSoldiersString = new StringBuilder();
        playerSoldiersString.append("Player" + ": " + player.getPlayerName() + "\t");

        //TODO change i+1 to real soldier id
        for (int i = 0; i < soldierList.length; i++) {
            if (!soldierList[i].isM_FinishedGame()) {
                playerSoldiersString.append("Soldier " + (i + 1) + ": " + (String.format("%02d", soldierList[i].getLocationOnBoard().getSquareNumber()) + "\t"));
            } else {
                playerSoldiersString.append("Soldier " + (i + 1) + ": " + "FINISHED" + "\t");
            }
        }
        playerSoldiersString.append(System.lineSeparator());
        System.out.print(playerSoldiersString.toString());
    }

    public void PrintCubeAnswer(int cubeAnswer) {
        System.out.println("Cube answer: " + cubeAnswer);
        System.out.println("Press 'Enter' to continue.");
        Scanner reader = new Scanner(System.in);
        String str = reader.nextLine();
    }

    public void ClearScreen() {
        for (int i = 0; i < 10; i++) {
            System.out.println("");
        }
    }

    public void displayXMLLoadError(eXMLLoadStatus loadStatus) {
        System.out.println("Error loading game from XML: " + loadStatus.toString());
    }

    public void printCurrentSoldier(int soldierIndex) {
        System.out.println("Current playing soldier: " + soldierIndex);
    }

    public void displayCurrPlayer(aPlayer player) {
        System.out.println(player.getPlayerName() + " is now playing.");
    }

}
