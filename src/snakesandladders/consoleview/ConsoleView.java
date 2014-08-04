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
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.ePlayerType;
import snakesandladders.xml.eXMLLoadStatus;
import snakesandladders.xml.eXMLSaveStatus;

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
        while ((input < 1) || (input > (boardSize * boardSize) / 5)) {
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
                return ePlayerType.HUMAN;
            case 2:
                return ePlayerType.COMPUTER;
            default:
                throw new SnakesAndLaddersRunTimeException("GetPlayerType(): Invalid PlayerType input");
        }
    }

    public String getPlayerString(List<SinglePlayer> playersList) {
        Scanner scanner = new Scanner(System.in);
        boolean playerExist = false;
        String playerName;
        do {
            System.out.println("Enter Player Name.");
            playerName = scanner.next();
            playerExist = findPlayerInArray(playerName, playersList);

            if (playerExist) {
                System.out.println("Current player name is already used. Enter different name.");
            }
        } while (playerExist);

        return playerName;

    }

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

    public void printGame(SnakesAndLaddersSingleGame o_Game, List<SinglePlayer> o_Players) throws SnakesAndLaddersRunTimeException {
        int singleGameBoardSize = o_Game.getO_BoardSize();
        List<SinglePlayer> players = o_Players;
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
        boardString.append("-----------------------------------------------------------------------").append(System.lineSeparator());
        for (SinglePlayer player : players) {
            boardString.append("Player " + player.getPlayerID() + ": " + player.getPlayerName() + "\t");
        }
        boardString.append(System.lineSeparator());

        //Print out the board
        System.out.print(boardString.toString());
    }

    public void displayWinner(String name) {
        System.out.println("The winner is: " + name);
    }

    public void printSnakesAndLaddersRunTimeExceptiom(SnakesAndLaddersRunTimeException ex) {
        System.out.println(ex.toString() + ": \n" + ex.getMessage());
    }

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

    public void displayLastMove(SinglePlayer player, BoardSquare move) {
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
        System.out.println("Second, please write the number of snakes and ladders (max: 20% of board):");
    }

    public int GetSoldierToPlayWith(SinglePlayer player) {
        Scanner scanner = new Scanner(System.in);
        int input;

        System.out.println(player);
        System.out.println("Choose soldier (1-4):");

        while (!scanner.hasNextInt()) {
            System.out.println("Inavlid Input. Please enter a number.");
            scanner.next();
        }

        input = scanner.nextInt();

        while ((input < 1 || input > 4) || (player.getM_SoldiersList().get(input - 1).isM_FinishedGame())) {
            System.out.println("Not Valid input, Please enter again:");
            System.out.println(player);
            System.out.println("Choose soldier (1-4):");

            while (!scanner.hasNextInt()) {
                System.out.println("Inavlid Input. Please enter a number.");
                scanner.next();
            }

            input = scanner.nextInt();
        }

        return input;
    }

    public void displaySoldiersOfPlayer(SinglePlayer player) {

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

    public void displayCurrPlayer(SinglePlayer player) {
        System.out.println(player.getPlayerName() + " is now playing.");
    }

    public void printGameName(String m_GameName) {
        System.out.println("Game name: " + m_GameName);
    }

    private boolean findPlayerInArray(String playerName, List<SinglePlayer> playersList) {
        for (SinglePlayer player : playersList) {
            if (player.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    public void displayXMLSaveError(eXMLSaveStatus saveStatus) {
        System.out.println("Error saving game to XML: " + saveStatus.toString());
    }

}
