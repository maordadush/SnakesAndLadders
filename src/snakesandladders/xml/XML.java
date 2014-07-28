/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.eChars;
import snakesandladders.players.ComputerPlayer;
import snakesandladders.players.HumanPlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.aPlayer;
import snakesandladders.players.ePlayerType;
import snl.*;
import snl.Cell.Soldiers;
import snl.Players.Player;

/**
 *
 * @author Noam
 */
public class XML {

    public static final String XSD_FOLDER = "/xsd/";
    public static final String XSD_FNAME = "snakesandladders.xsd";

    public static eXMLLoadStatus loadXML(String xmlPath, GameModel model, List<aPlayer> localPlayers) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema;

        URL xsdURL = XML.class.getResource(XSD_FOLDER + XSD_FNAME);
        if (xsdURL == null) {
            return eXMLLoadStatus.XSD_FILE_NOT_FOUND;
        }

        try {
            schema = schemaFactory.newSchema(new File(xsdURL.getFile()));
        } catch (SAXException ex) {
            return eXMLLoadStatus.XSD_FILE_NOT_FOUND;
        }

        try {
            JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
            Unmarshaller u = jc.createUnmarshaller();
            u.setSchema(schema);

            File file = new File(xmlPath);

            Snakesandladders snakesandladders = (Snakesandladders) u.unmarshal(file);

            eXMLLoadStatus loadStatus;

            loadStatus = loadPlayers(snakesandladders.getPlayers().getPlayer(), snakesandladders.getCurrentPlayer(), model);
            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }

//            loadStatus = loadGameName(Snakesandladders.getName(), model);
//            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
//                return loadStatus;
//            }

//            loadStatus = loadGameBoards(Snakesandladders.getBoard().getBoard(), model);
//            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
//                return loadStatus;
//            }
//            loadStatus = loadNumberOfSoldiers(Snakesandladders.getNumberOfSoldiers().getBoard(), model);
//            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
//                return loadStatus;
//            }
        } catch (JAXBException ex) {
            if (ex.getLinkedException() instanceof FileNotFoundException) {
                return eXMLLoadStatus.XML_FILE_NOT_FOUND;
            }
            if (ex.getLinkedException() instanceof SAXParseException) {
                return eXMLLoadStatus.NOT_VALID_XML;
            }
            return eXMLLoadStatus.GENERAL_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadPlayers(List<Player> players, String currPlayer, GameModel model) {
        ePlayerType[] playerTypes = new ePlayerType[players.size()];
        String[] playerNames = new String[players.size()];
        
        for (int i = 0; i < playerTypes.length; i++) {
            try {
                playerTypes[i] = ePlayerType.GetTypeFromXML(players.get(i).getType());
            } catch (XMLException ex) {
                return eXMLLoadStatus.PLAYER_TYPE;
            }

//            try {
                playerNames[i] = players.get(i).getName();
//            } catch (XMLException ex) {
//                return eXMLLoadStatus.PLAYER_NAME_ERROR;
//            }
        }
        
        for (int i = 0; i < playerNames.length; i++) {
            for (int j = i + 1; j < playerNames.length; j++) {
                if (playerNames[i].equals(playerNames[j])) {
                    return eXMLLoadStatus.PLAYERS_DUPLICATE_NAME;
                }
            }
        }

        try {
            for (int i = 0; i < playerNames.length; i++) {
                
                switch (playerTypes[i]) {
                    case Human:
                        if (players.get(i).getName() != null) {
                            model.addPlayer(new HumanPlayer(playerNames[i],4)); 
                        }
                        break;
                    case Computer:
                        if (players.get(i).getName() != null) {
                            model.addPlayer(new ComputerPlayer(players.get(i).getName(), 4)); 
                        } 
                        break;
                    default:
                        return eXMLLoadStatus.PLAYER_TYPE;                       
                } 
                if (players.get(i).getName().equals(currPlayer)){
                    model.setCurrPlayer(currPlayer);
                }
            }
        } catch (SnakesAndLaddersRunTimeException ex) {
            return eXMLLoadStatus.ADD_PLAYER_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

//    private static XMLLoadStatus loadGameInfo(Tictactoe.Game game, GameModel model) {
//        if (game.getCurrentBoard() != null) {
//            int col = game.getCurrentBoard().getCol();
//            int row = game.getCurrentBoard().getRow();
//            SquareIndex currGame = new SquareIndex(row - 1, col - 1);
//            try {
//                model.setGameIndex(currGame);
//                model.setSelectNextGame(false);
//            } catch (XMixDrixRunTimeException ex) {
//                return XMLLoadStatus.WRONG_GAME_INDEX;
//            }
//        } else {
//            model.setSelectNextGame(true);
//        }
//
//        XMixDrixChars currPlayerChar;
//        try {
//            currPlayerChar = XMixDrixChars.getCharFromXML(game.getCurrentTurn());
//        } catch (XMLException ex) {
//            return XMLLoadStatus.CHAR_ERROR;
//        }
//
//        try {
//            model.setCurrentTurnPlayerFromChar(currPlayerChar);
//        } catch (XMixDrixRunTimeException ex) {
//            return XMLLoadStatus.CURR_TURN_PLAYER_ERROR;
//        }
//
//        return XMLLoadStatus.LOAD_SUCCESS;
//    }
//
//    private static void zeroMat(int mat[][], int size) {
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                mat[i][j] = 0;
//            }
//        }
//    }
//
//    private static XMLLoadStatus loadGameBoards(List<Board> boards, GameModel model) {
//        XMLLoadStatus loadStatus;
//        int gameBoards[][] = new int[XMixDrixSingleGame.BOARD_SIZE][XMixDrixSingleGame.BOARD_SIZE];
//
//        zeroMat(gameBoards, XMixDrixSingleGame.BOARD_SIZE);
//
//        for (Board board : boards) {
//            if (gameBoards[board.getRow() - 1][board.getCol() - 1] == 1) {
//                return XMLLoadStatus.DUPLICATE_BOARD;
//            }
//            gameBoards[board.getRow() - 1][board.getCol() - 1] = 1;
//
//            loadStatus = loadBoard(board, model);
//            if (loadStatus != XMLLoadStatus.LOAD_SUCCESS) {
//                return loadStatus;
//            }
//        }
//
//        return XMLLoadStatus.LOAD_SUCCESS;
//    }
//
//    private static XMLLoadStatus loadBoard(Board board, GameModel model) {
//        SquareIndex gameIndex = new SquareIndex(board.getRow() - 1, board.getCol() - 1);
//        SquareIndex setCell = new SquareIndex();
//        XMixDrixChars cellChar;
//        List<Cell> cells = board.getCell();
//        XMixDrixChars winnerChar;
//
//        for (Cell cell : cells) {
//            setCell.setSquare(cell.getRow() - 1, cell.getCol() - 1);
//            try {
//                cellChar = XMixDrixChars.getCharFromXML(cell.getValue());
//            } catch (XMLException ex) {
//                return XMLLoadStatus.CHAR_ERROR;
//            }
//            try {
//                model.loadSquareToGame(gameIndex, setCell, cellChar);
//            } catch (XMixDrixRunTimeException ex) {
//                return XMLLoadStatus.DUPLICATE_CELLS;
//            }
//        }
//
//        try {
//            if (board.getWinner() == null) {
//                winnerChar = XMixDrixChars.NONE;
//            } else {
//                winnerChar = XMixDrixChars.getCharFromXML(board.getWinner());
//            }
//        } catch (XMLException ex) {
//            return XMLLoadStatus.WINNER_ERROR;
//        }
//
//        if (model.validateWinner(gameIndex, winnerChar) == false) {
//            return XMLLoadStatus.WINNER_ERROR;
//        }
//
//        return XMLLoadStatus.LOAD_SUCCESS;
//    }
//
//    public static XMLSaveStatus saveXML(String savePath, GameModel model) {
//        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        Schema schema;
//
//        URL xsdURL = XML.class.getResource(XSD_FOLDER + XSD_FNAME);
//        if (xsdURL == null) {
//            return XMLSaveStatus.XSD_FILE_NOT_FOUND;
//        }
//
//        try {
//            schema = schemaFactory.newSchema(new File(xsdURL.getFile()));
//        } catch (SAXException ex) {
//            return XMLSaveStatus.XSD_FILE_NOT_FOUND;
//        }
//
//        try {
//            JAXBContext jc = JAXBContext.newInstance(Tictactoe.class);
//            Marshaller u = jc.createMarshaller();
//            u.setSchema(schema);
//            u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            File file = new File(savePath);
//
//            Tictactoe tictactoe = new Tictactoe();
//
//            tictactoe.setPlayers(getPlayer(model));
//            tictactoe.setGame(getGame(model));
//            tictactoe.setBoards(getBoards(model));
//
//            u.marshal(tictactoe, file);
//
//        } catch (JAXBException | XMLException | XMixDrixRunTimeException ex) {
//            return XMLSaveStatus.GENERAL_ERROR;
//        }
//
//        return XMLSaveStatus.SAVE_SUCCESS;
//    }
//
//    private static GameValue getGameValue(XMixDrixChars playerChar) throws XMLException {
//        switch (playerChar) {
//            case X:
//                return GameValue.X;
//            case O:
//                return GameValue.O;
//            case X_WIN:
//                return GameValue.X;
//            case O_WIN:
//                return GameValue.O;
//            default:
//                throw new XMLException("GetGameValue(): Invalid playerChar.");
//        }
//    }
//
//    private static PlayerType getPlayerType(XMixDrixPlayer player) throws XMLException {
//        if (player instanceof HumanPlayer) {
//            return PlayerType.HUMAN;
//        }
//        if (player instanceof ComputerPlayer) {
//            return PlayerType.COMPUTER;
//        }
//
//        throw new XMLException("GetPlayerType(): Invalid playerType.");
//    }
//
//    private static Tictactoe.Players getPlayer(GameModel model) throws XMLException {
//        Tictactoe.Players ticPlayers = new Tictactoe.Players();
//        XMixDrixPlayer player;
//        GameValue gameValue;
//        PlayerType playerType;
//        Player addPlayer;
//
//        for (int i = 0; i < GameModel.MAX_PLAYERS; i++) {
//            player = model.getPlayer(i);
//            gameValue = getGameValue(player.getPlayerChar());
//            playerType = getPlayerType(player);
//            addPlayer = new Player();
//            addPlayer.setType(playerType);
//            addPlayer.setValue(gameValue);
//            addPlayer.setName(player.getPlayerName());
//            ticPlayers.getPlayer().add(addPlayer);
//        }
//
//        return ticPlayers;
//    }
//
//    private static Tictactoe.Game getGame(GameModel model) throws XMLException {
//        Tictactoe.Game game = new Tictactoe.Game();
//        Tictactoe.Game.CurrentBoard currBoard = new Tictactoe.Game.CurrentBoard();
//        SquareIndex currGameIndex = model.getCurrGameIndex();
//        currBoard.setRow(currGameIndex.getX() + 1);
//        currBoard.setCol(currGameIndex.getY() + 1);
//        game.setCurrentBoard(currBoard);
//
//        XMixDrixPlayer player = model.getCurrPlayer();
//        GameValue currTurn = getGameValue(player.getPlayerChar());
//        game.setCurrentTurn(currTurn);
//
//        return game;
//    }
//
//    private static Tictactoe.Boards getBoards(GameModel model) throws XMLException, XMixDrixRunTimeException {
//        Tictactoe.Boards tictactoeBoards = new Tictactoe.Boards();
//        Board board;
//        SquareIndex gameIndex = new SquareIndex();
//        for (int i = 0; i < XMixDrixSingleGame.BOARD_SIZE; i++) {
//            for (int j = 0; j < XMixDrixSingleGame.BOARD_SIZE; j++) {
//                gameIndex.setSquare(i, j);
//                board = getBoard(gameIndex, model);
//                if (board != null) {
//                    tictactoeBoards.getBoard().add(board);
//                }
//            }
//        }
//
//        return tictactoeBoards;
//    }
//
//    private static Board getBoard(SquareIndex gameIndex, GameModel model) throws XMLException, XMixDrixRunTimeException {
//        Cell cell;
//        GameValue gameValue;
//        XMixDrixChars gameChar;
//        SquareIndex squareIndex = new SquareIndex();
//        Board board = new Board();
//
//        board.setRow(gameIndex.getX() + 1);
//        board.setCol(gameIndex.getY() + 1);
//
//        for (int i = 0; i < XMixDrixSingleGame.BOARD_SIZE; i++) {
//            for (int j = 0; j < XMixDrixSingleGame.BOARD_SIZE; j++) {
//                squareIndex.setSquare(i, j);
//                gameChar = model.getGame().getCharFromGame(gameIndex, squareIndex);
//                if (gameChar != XMixDrixChars.NONE) {
//                    gameValue = getGameValue(gameChar);
//                    cell = new Cell();
//                    cell.setRow(i + 1);
//                    cell.setCol(j + 1);
//                    cell.setValue(gameValue);
//                    board.getCell().add(cell);
//                }
//            }
//        }
//        gameChar = model.getWinnerFromGame(gameIndex);
//        if (gameChar != XMixDrixChars.NONE) {
//            board.setWinner(getGameValue(gameChar));
//        }
//        if (board.getCell().isEmpty()) {
//            return null;
//        }
//
//        return board;
//    }
}
