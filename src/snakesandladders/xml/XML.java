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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.eChars;
import snakesandladders.players.ComputerPlayer;
import snakesandladders.players.HumanPlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.aPlayer;
import snakesandladders.players.ePlayerType;
import snl.*;
import snl.Cell.Soldiers;
import snl.Ladders.Ladder;
import snl.Players.Player;
import snl.Snakes.Snake;

/**
 *
 * @author Noam
 */
public class XML {

    public static final String XSD_FOLDER = "/xsd/";
    public static final String XSD_FNAME = "snakesandladders.xsd";
    private static int m_GameSize;
    private static int m_NumOfSnakesAndLadders;
    private static int m_NumOfPlayers;
    private static int m_NumOfSoldiersToWin;

    private static SchemaFactory schemaFactory;
    private static Schema schema;
    private static URL xsdURL;
    private static JAXBContext jc;
    private static Unmarshaller u;
    private static File file;
    private static Snakesandladders snakesandladders;
    private static eXMLLoadStatus loadStatus;

    public static eXMLLoadStatus initModelFromXml(String xmlPath, GameModel model) {
        schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        xsdURL = XML.class.getResource(XSD_FOLDER + XSD_FNAME);
        if (xsdURL == null) {
            return eXMLLoadStatus.XSD_FILE_NOT_FOUND;
        }

        try {
            schema = schemaFactory.newSchema(new File(xsdURL.getFile()));
        } catch (SAXException ex) {
            return eXMLLoadStatus.XSD_FILE_NOT_FOUND;
        }

        try {
            jc = JAXBContext.newInstance(Snakesandladders.class);
            u = jc.createUnmarshaller();
            u.setSchema(schema);

            file = new File(xmlPath);

            snakesandladders = (Snakesandladders) u.unmarshal(file);

            loadStatus = initModel(snakesandladders.getPlayers().getPlayer().size(),
                    snakesandladders.getBoard().getSize(), snakesandladders.getBoard().getSnakes().getSnake().size(),
                    snakesandladders.getBoard().getLadders().getLadder().size(), model);

            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }
            m_GameSize = snakesandladders.getBoard().getSize();
            m_NumOfPlayers = snakesandladders.getPlayers().getPlayer().size();
            m_NumOfSnakesAndLadders = snakesandladders.getBoard().getLadders().getLadder().size()
                    + snakesandladders.getBoard().getSnakes().getSnake().size();
            
            loadStatus = loadNumberOfSoldiersToWin(snakesandladders.getNumberOfSoldiers());
            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }
            m_NumOfSoldiersToWin = snakesandladders.getNumberOfSoldiers();
            
            return eXMLLoadStatus.LOAD_SUCCESS;
        } catch (JAXBException ex) {
            if (ex.getLinkedException() instanceof FileNotFoundException) {
                return eXMLLoadStatus.XML_FILE_NOT_FOUND;
            }
            if (ex.getLinkedException() instanceof SAXParseException) {
                return eXMLLoadStatus.NOT_VALID_XML;
            }
            return eXMLLoadStatus.GENERAL_ERROR;
        }
    }

    public static eXMLLoadStatus loadXML(String xmlPath, GameModel model, int O_NumOfSoldiersToWin) {

        loadStatus = loadPlayers(snakesandladders.getPlayers().getPlayer(), snakesandladders.getCurrentPlayer(), model);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            return loadStatus;
        }

        loadStatus = loadGameBoard(snakesandladders.getBoard(), model);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            return loadStatus;
        }

        loadStatus = loadSoldiers(snakesandladders.getBoard().getCells().getCell(), model);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            return loadStatus;
        }

        loadStatus = loadCurrentPlayer(snakesandladders.getCurrentPlayer(), model);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            return loadStatus;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    public static int getM_NumOfSnakesAndLadders() {
        return m_NumOfSnakesAndLadders;
    }

    public static int getM_NumOfPlayers() {
        return m_NumOfPlayers;
    }

    public static int getM_GameSize() {
        return m_GameSize;
    }

    public static int getM_NumOfSoldiersToWin() {
        return m_NumOfSoldiersToWin;
    }

    private static eXMLLoadStatus loadPlayers(List<Player> players, String currPlayer, GameModel model) {
        for (Player player : players) {
            switch (player.getType()) {
                case HUMAN:
                    try {
                        model.addPlayer(new HumanPlayer(player.getName()));
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        return eXMLLoadStatus.ADD_PLAYER_ERROR;
                    }
                    break;
                case COMPUTER:
                    try {
                        model.addPlayer(new ComputerPlayer(player.getName()));
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        return eXMLLoadStatus.ADD_PLAYER_ERROR;
                    }
                    break;
                default:
                    return eXMLLoadStatus.PLAYER_NAME_ERROR;
            }

            if (player.getName().equals(currPlayer)) {
                try {
                    model.setCurrPlayer(currPlayer);
                } catch (SnakesAndLaddersRunTimeException ex) {
                    return eXMLLoadStatus.CURR_TURN_PLAYER_ERROR;
                }
            }
        }
        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadGameBoard(Board board, GameModel model) {
        eXMLLoadStatus loadStatus;
        int boardSize = board.getSize();
        List<Ladder> ladders = board.getLadders().getLadder();
        List<Snake> snakes = board.getSnakes().getSnake();

        //init Board
        if (boardSize < 5 || boardSize > 8) {
            return eXMLLoadStatus.BOARD_SIZE_ERROR;
        }
        model.getGame().setO_BoardSize(boardSize);
        model.initGame();

        //Init snakes and ladders
        if (ladders.size() != snakes.size()) {
            return eXMLLoadStatus.SNAKES_LADDERS_ERROR;
        }
        readSnakesAndLadders(ladders, snakes, model);

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus readSnakesAndLadders(List<Ladder> ladders, List<Snake> snakes, GameModel model) {
        BoardSquare from;
        BoardSquare to;
        for (Snake snake : snakes) {
            from = model.getGame().getBoardSquare(snake.getFrom().intValue());
            to = model.getGame().getBoardSquare(snake.getTo().intValue());
            if (!model.getGame().setSnake(from, to)) {
                return eXMLLoadStatus.SNAKES_LADDERS_ERROR;
            }
        }
        for (Ladder ladder : ladders) {
            from = model.getGame().getBoardSquare(ladder.getFrom().intValue());
            to = model.getGame().getBoardSquare(ladder.getTo().intValue());
            if (!model.getGame().setLadder(from, to)) {
                return eXMLLoadStatus.SNAKES_LADDERS_ERROR;
            }
        }
        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus initModel(int o_NumOfPlayers, int o_BoarsSize, int o_NumOfSnakes, int o_NumOfLadders, GameModel model) {
        if (o_NumOfLadders != o_NumOfSnakes) {
            return eXMLLoadStatus.SNAKES_LADDERS_ERROR;
        }
        if (o_NumOfPlayers < 2 || o_NumOfPlayers > 4) {
            return eXMLLoadStatus.ADD_PLAYER_ERROR;
        }
        if (o_BoarsSize < 5 || o_BoarsSize > 8) {
            return eXMLLoadStatus.BOARD_SIZE_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadNumberOfSoldiersToWin(int o_NumberOfSoldiers) {
        if (o_NumberOfSoldiers > 4 || o_NumberOfSoldiers < 1) {
            return eXMLLoadStatus.SOLDIERS_TO_WIN_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadSoldiers(List<Cell> o_CellsList, GameModel model) {
        for (aPlayer player : model.getPlayers()) {
            player.initSoldiers(model.getCurrGameIndex());
        }
        for (Cell cell : o_CellsList) {
            List<Soldiers> soldiersList = cell.getSoldiers();
            BoardSquare currCell = model.getGame().getBoardSquare(cell.getNumber().intValue());
            for (Soldiers soldier : soldiersList) {
                aPlayer player = model.getPlayerByName(soldier.getPlayerName());
                try {
                    player.placeSoldierOnBoard(currCell);
                } catch (SnakesAndLaddersRunTimeException ex) {
                    return eXMLLoadStatus.SOLDIER_LIST_NOT_INITED;
                }
            }
        }
        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadCurrentPlayer(String o_CurrentPlayer, GameModel model) {
        for (aPlayer player : model.getPlayers()) {
            if (player.getPlayerName().equals(o_CurrentPlayer)) {
                model.setCurrPlayer(player);
            }
        }
        if (model.getCurrPlayer() == null) {
            return eXMLLoadStatus.CURR_TURN_PLAYER_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

}
