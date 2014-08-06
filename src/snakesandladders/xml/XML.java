/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.URL;
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
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.eChars;
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;
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
            m_NumOfSoldiersToWin = snakesandladders.getNumberOfSoldiers();

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

    public static eXMLLoadStatus loadXML(String xmlPath, GameModel model) {

        loadStatus = loadPlayers(snakesandladders.getPlayers().getPlayer(), snakesandladders.getCurrentPlayer(), model);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            return loadStatus;
        }

        loadStatus = loadGameBoard(snakesandladders.getBoard(), snakesandladders.getName(), model);
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
        boolean playerExist = false;

        for (Player player : players) {
            playerExist = findPlayerInArray(player, model.getPlayers());
            if (playerExist) {
                return eXMLLoadStatus.PLAYERS_DUPLICATE_NAME;
            }

            switch (player.getType()) {
                case HUMAN:
                    try {
                        model.addPlayer(new SinglePlayer(player.getName(), ePlayerType.HUMAN));
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        return eXMLLoadStatus.ADD_PLAYER_ERROR;
                    }
                    break;
                case COMPUTER:
                    try {
                        model.addPlayer(new SinglePlayer(player.getName(), ePlayerType.COMPUTER));
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

        if (model.getCurrPlayer() == null) {
            return eXMLLoadStatus.CURR_TURN_PLAYER_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadGameBoard(Board board, String gameName, GameModel model) {
        int boardSize = board.getSize();
        List<Ladder> ladders = board.getLadders().getLadder();
        List<Snake> snakes = board.getSnakes().getSnake();

        //init Board
        if (boardSize < 5 || boardSize > 8) {
            return eXMLLoadStatus.BOARD_SIZE_ERROR;
        }
        model.setM_GameName(gameName);
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
        if (o_NumOfPlayers < 2 || o_NumOfPlayers > 4) {
            return eXMLLoadStatus.ADD_PLAYER_ERROR;
        }
        if (o_BoarsSize < 5 || o_BoarsSize > 8) {
            return eXMLLoadStatus.BOARD_SIZE_ERROR;
        }
        if (o_NumOfLadders != o_NumOfSnakes || o_NumOfLadders > o_BoarsSize * o_BoarsSize / 5) {
            return eXMLLoadStatus.SNAKES_LADDERS_ERROR;
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
        for (Cell cell : o_CellsList) {
            List<Soldiers> soldiersList = cell.getSoldiers();
            BoardSquare currCell = model.getGame().getBoardSquare(cell.getNumber().intValue());
            for (Soldiers soldier : soldiersList) {
                SinglePlayer player = model.getPlayerByName(soldier.getPlayerName());
                for (int i = 0; i < soldier.getCount(); i++) {
                    Soldier newSoldier = new Soldier(player.getColor(), i + 1, currCell);
                    if (currCell.getSquareNumber() == model.GetSingleGame().getMAX_SQUARE_NUM()) {
                        newSoldier.setM_FinishedGame(true);
                    }
                    player.getM_SoldiersList().add(newSoldier);
                }
                currCell.getPlayers().add(player);
            }
        }
        for (SinglePlayer player : model.getPlayers()) {
            if (player.getM_SoldiersList().size() != player.NUM_OF_SOLDIERS) {
                return eXMLLoadStatus.ILLIGAL_NUM_OF_SOLDIERS;
            }
        }
        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadCurrentPlayer(String o_CurrentPlayer, GameModel model) {
        for (SinglePlayer player : model.getPlayers()) {
            if (player.getPlayerName().equals(o_CurrentPlayer)) {
                model.setCurrPlayer(player);
            }
        }
        if (model.getCurrPlayer() == null) {
            return eXMLLoadStatus.CURR_TURN_PLAYER_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    public static eXMLSaveStatus saveXML(String savePath, GameModel model) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema;

        URL xsdURL = XML.class.getResource(XSD_FOLDER + XSD_FNAME);
        if (xsdURL == null) {
            return eXMLSaveStatus.XSD_FILE_NOT_FOUND;
        }

        try {
            schema = schemaFactory.newSchema(new File(xsdURL.getFile()));
        } catch (SAXException ex) {
            return eXMLSaveStatus.XSD_FILE_NOT_FOUND;
        }

        try {
            JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
            Marshaller u = jc.createMarshaller();
            u.setSchema(schema);
            u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(savePath);

            Snakesandladders snakesandladders = new Snakesandladders();

            snakesandladders.setName(getGameName(model));
            snakesandladders.setNumberOfSoldiers(getNumOfSoldiers(model));
            snakesandladders.setCurrentPlayer(getCurrPlayer(model));
            snakesandladders.setPlayers(getPlayer(model));
            snakesandladders.setBoard(getBoard(model));

            u.marshal(snakesandladders, file);

        } catch (JAXBException | XMLException ex) {
            return eXMLSaveStatus.GENERAL_ERROR;
        }

        return eXMLSaveStatus.SAVE_SUCCESS;
    }

    private static String getGameName(GameModel model) throws XMLException {
        return model.getM_GameName();
    }

    private static int getNumOfSoldiers(GameModel model) throws XMLException {
        return model.getM_NumOfSoldiersToWin();
    }

    private static String getCurrPlayer(GameModel model) throws XMLException {
        return model.getCurrPlayer().getPlayerName();
    }

    private static Players getPlayer(GameModel model) throws XMLException {
        Players players = new Players();
        for (SinglePlayer player : model.getPlayers()) {
            Players.Player newPlayer = new Players.Player();
            newPlayer.setName(player.getPlayerName());
            switch (player.getType()) {
                case HUMAN:
                    newPlayer.setType(PlayerType.HUMAN);
                    break;
                case COMPUTER:
                    newPlayer.setType(PlayerType.COMPUTER);
                    break;
            }
            players.getPlayer().add(newPlayer);
        }
        return players;
    }

    private static Board getBoard(GameModel model) throws XMLException {
        Board newBoard = new Board();
        Cells cells = new Cells();
        Snakes snakes = new Snakes();
        Ladders ladders = new Ladders();
        int boardSize = model.getGame().getO_BoardSize();
        // set the board size
        newBoard.setSize(boardSize);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                BoardSquare currBoardSquare = model.getGame().getBoardSquare(i, j);
                if (!currBoardSquare.getPlayers().isEmpty()) {
                    Cell cell = new Cell();
                    cell.setNumber(BigInteger.valueOf(currBoardSquare.getSquareNumber()));
                    for (SinglePlayer player : currBoardSquare.getPlayers()) {
                        int numSoldiersAtSquare = 0;
                        for (Soldier soldier : player.getM_SoldiersList()) {
                            if (soldier.getLocationOnBoard() == currBoardSquare) {
                                numSoldiersAtSquare++;
                            }
                        }
                        if (numSoldiersAtSquare > 0) {
                            Cell.Soldiers newPlayer = new Cell.Soldiers();
                            newPlayer.setPlayerName(player.getPlayerName());
                            newPlayer.setCount(numSoldiersAtSquare);
                            cell.getSoldiers().add(newPlayer);
                        }
                    }
                    cells.getCell().add(cell);
                }
                // add ladders or snakes
                eChars currCellType = currBoardSquare.getType();
                if (currCellType == eChars.SNAKE_HEAD) {
                    Snake newSnake = new Snake();
                    newSnake.setFrom(BigInteger.valueOf(currBoardSquare.getSquareNumber()));
                    newSnake.setTo(BigInteger.valueOf(currBoardSquare.getJumpTo().getSquareNumber()));
                    snakes.getSnake().add(newSnake);
                } else if (currCellType == eChars.LADDER_TAIL) {
                    Ladder newLadder = new Ladder();
                    newLadder.setFrom(BigInteger.valueOf(currBoardSquare.getSquareNumber()));
                    newLadder.setTo(BigInteger.valueOf(currBoardSquare.getJumpTo().getSquareNumber()));
                    ladders.getLadder().add(newLadder);
                }
            }
        }
        newBoard.setCells(cells);
        newBoard.setLadders(ladders);
        newBoard.setSnakes(snakes);
        return newBoard;
    }

    private static boolean findPlayerInArray(Player player, List<SinglePlayer> players) {
        for (SinglePlayer currPlayer : players) {
            if (currPlayer.getPlayerName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

}
