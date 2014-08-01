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
    
    private static SchemaFactory schemaFactory;
    private static Schema schema;
    private static URL xsdURL;
    private static JAXBContext jc;
    private static Unmarshaller u;
    private static File file;
    private static Snakesandladders snakesandladders;
    private static eXMLLoadStatus loadStatus;

    public static eXMLLoadStatus initModelFromXml(String xmlPath, GameModel model, int O_NumOfSoldiersToWin) {
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
            m_NumOfSnakesAndLadders = snakesandladders.getBoard().getLadders().getLadder().size();

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

//        schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//
//        xsdURL = XML.class.getResource(XSD_FOLDER + XSD_FNAME);
//        if (xsdURL == null) {
//            return eXMLLoadStatus.XSD_FILE_NOT_FOUND;
//        }
//
//        try {
//            schema = schemaFactory.newSchema(new File(xsdURL.getFile()));
//        } catch (SAXException ex) {
//            return eXMLLoadStatus.XSD_FILE_NOT_FOUND;
//        }

//        try {
//            jc = JAXBContext.newInstance(Snakesandladders.class);
//            u = jc.createUnmarshaller();
//            u.setSchema(schema);
//
//            file = new File(xmlPath);
//
//            snakesandladders = (Snakesandladders) u.unmarshal(file);

            loadStatus = loadNumberOfSoldiersToWin(snakesandladders.getNumberOfSoldiers(), O_NumOfSoldiersToWin);
            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }

            loadStatus = loadPlayers(snakesandladders.getPlayers().getPlayer(), snakesandladders.getCurrentPlayer(), model);
            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }

            loadStatus = loadSoldiers(snakesandladders.getBoard().getCells().getCell(), model);
            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }

//            loadStatus = loadGameName(Snakesandladders.getName(), model);
//            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
//                return loadStatus;
//            }
            loadStatus = loadCurrentPlayer(snakesandladders.getCurrentPlayer(), model);
            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }            

            loadStatus = loadGameBoard(snakesandladders.getBoard(), model);
            if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
                return loadStatus;
            }
//        } catch (JAXBException ex) {
//            if (ex.getLinkedException() instanceof FileNotFoundException) {
//                return eXMLLoadStatus.XML_FILE_NOT_FOUND;
//            }
//            if (ex.getLinkedException() instanceof SAXParseException) {
//                return eXMLLoadStatus.NOT_VALID_XML;
//            }
//            return eXMLLoadStatus.GENERAL_ERROR;
//        }

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

        //Noam: "Can replace with function 'Find' of java" and check number od players
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
                            model.addPlayer(new HumanPlayer(playerNames[i]));
                        }
                        break;
                    case Computer:
                        if (players.get(i).getName() != null) {
                            model.addPlayer(new ComputerPlayer(players.get(i).getName()));
                        }
                        break;
                    default:
                        return eXMLLoadStatus.PLAYER_TYPE;
                }
                if (players.get(i).getName().equals(currPlayer)) {
                    model.setCurrPlayer(currPlayer);
                }
            }
        } catch (SnakesAndLaddersRunTimeException ex) {
            return eXMLLoadStatus.ADD_PLAYER_ERROR;
        }

        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadGameBoard(Board board, GameModel model) {
        eXMLLoadStatus loadStatus;
        int boardSize = board.getSize();
        List<Ladder> ladders = board.getLadders().getLadder();
        List<Snake> snakes = board.getSnakes().getSnake();

        model.initGame();
        readSnakesAndLadders(board, model);

//        loadStatus = loadBoard(board, model);
//        if (loadStatus != XMLLoadStatus.LOAD_SUCCESS) {
//            return loadStatus;
//        }
        return eXMLLoadStatus.LOAD_SUCCESS;
    }

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
    private static void readSnakesAndLadders(Board board, GameModel model) {
        for (Ladder ladder : board.getLadders().getLadder()) {
            if (LadderIsLegal(ladder, model)) {
//                //Noam: "dadush keep from here - implement set ladders and snakes as you did in shuffle"
//                BoardSquare from = model.getGame().getBoardSquare(ladder.getFrom());
//                BoardSquare to = model.getGame().getBoardSquare(ladder.getTo());
//                
//                from.setJumpTo(to);
//                from.setType(eChars.LADDER_TAIL);
//                to.setType(eChars.LADDER_HEAD);
            }
        }
    }

    private static boolean LadderIsLegal(Ladder ladder, GameModel model) {
        //Noam: "Also need to check if ladder head\tail index is not NONE
        boolean returnedValue = true;
        if ((ladder.getFrom().intValue() > model.getGame().getO_BoardSize())
                || ladder.getTo().intValue() > model.getGame().getO_BoardSize()) {
            returnedValue = false;
        }

        return returnedValue;
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

    private static eXMLLoadStatus loadNumberOfSoldiersToWin(int o_NumberOfSoldiers, int o_LocalNumOfSoldiersToWin) {
        if (o_NumberOfSoldiers > 4 || o_NumberOfSoldiers < 1) {
            return eXMLLoadStatus.SOLDIERS_TO_WIN_ERROR;
        }

        o_LocalNumOfSoldiersToWin = o_NumberOfSoldiers;
        return eXMLLoadStatus.LOAD_SUCCESS;
    }

    private static eXMLLoadStatus loadSoldiers(List<Cell> o_CellsList, GameModel o_Model) {
        for (Cell cell : o_CellsList) {
            for (Soldiers soldier : cell.getSoldiers()) {
                for (aPlayer player : o_Model.getPlayers()) {
                    if (player.getPlayerName().equals(soldier.getPlayerName())) {
                        for (int i = 0; i < soldier.getCount(); i++) {
//                            //Noam: "Dadush - write getBoardSquare that get only 1 number of index"
//                            player.getCurrentSoldier().setLocationOnBoard(o_Model.getGame().getBoardSquare(cell.getNumber());
                            player.ForwardCurrentSoldier();
                        }
                    }
                }
            }
        }

        for (aPlayer player : o_Model.getPlayers()) {
            if (player.getM_SoldiersList().length != 4) {
                return eXMLLoadStatus.ILLIGAL_NUM_OF_SOLDIERS;
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
