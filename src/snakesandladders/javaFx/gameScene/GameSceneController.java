/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.gameScene;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.Cube;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;
import snakesandladders.javaFx.components.BoardView;
import snakesandladders.javaFx.components.ImageManager;
import snakesandladders.javaFx.components.PlayerView;
import snakesandladders.javaFx.components.SquareView;
import snakesandladders.javaFx.initScene.SceneInitController;
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.ePlayerType;
import static snakesandladders.players.ePlayerType.COMPUTER;
import snakesandladders.xml.eXMLLoadStatus;

/**
 * FXML Controller class
 *
 * @author Noam
 */
public class GameSceneController implements Initializable {

    boolean shuffleLaddersAndSnakes = true;
    Cube cube;
    int cubeAnswer;

    @FXML
    private Font x1;
    @FXML
    private Color x2;
    @FXML
    private Font x3;
    private GameModel model;
    @FXML
    private Label labelCurrPlayer;
    @FXML
    private Button buttonPlay;
    @FXML
    private VBox playersPane;
    @FXML
    private AnchorPane boardPane;
    private SimpleBooleanProperty selectedNewGame;
    private SimpleBooleanProperty openGameSelected;
    private SimpleBooleanProperty closeGame;
    private SimpleBooleanProperty saveGameSelected;
    private SimpleBooleanProperty saveGameAsSelected;
    private SimpleBooleanProperty quitGameSelected;
    private SimpleBooleanProperty helpSelected;
    private SimpleBooleanProperty userChooseSoldier1;
    private SimpleBooleanProperty userChooseSoldier2;
    private SimpleBooleanProperty userChooseSoldier3;
    private SimpleBooleanProperty userChooseSoldier4;

    private List<ImageView> m_ImageViewSoldiers;
    private List<Label> m_LablesSoldiers;
    @FXML
    private ImageView imageViewSoldier1;
    @FXML
    private ImageView imageViewSoldier2;
    @FXML
    private ImageView imageViewSoldier4;
    @FXML
    private ImageView imageViewSoldier3;
    @FXML
    private Label labelIndexSoldier1;
    @FXML
    private Label labelIndexSoldier2;
    @FXML
    private Label labelIndexSoldier3;
    @FXML
    private Label labelIndexSoldier4;
    @FXML
    private Label labelCubeAnswer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        playersPane.setSpacing(10);

        selectedNewGame = new SimpleBooleanProperty(false);
        openGameSelected = new SimpleBooleanProperty(false);
        closeGame = new SimpleBooleanProperty(false);
        saveGameSelected = new SimpleBooleanProperty(false);
        saveGameAsSelected = new SimpleBooleanProperty(false);
        quitGameSelected = new SimpleBooleanProperty(false);
        helpSelected = new SimpleBooleanProperty(false);
        userChooseSoldier1 = new SimpleBooleanProperty(false);
        userChooseSoldier2 = new SimpleBooleanProperty(false);
        userChooseSoldier3 = new SimpleBooleanProperty(false);
        userChooseSoldier4 = new SimpleBooleanProperty(false);

        cube = new Cube();

        m_ImageViewSoldiers = new ArrayList<ImageView>();
        m_ImageViewSoldiers.add(imageViewSoldier1);
        m_ImageViewSoldiers.add(imageViewSoldier2);
        m_ImageViewSoldiers.add(imageViewSoldier3);
        m_ImageViewSoldiers.add(imageViewSoldier4);

        m_LablesSoldiers = new ArrayList<>();
        m_LablesSoldiers.add(labelIndexSoldier1);
        m_LablesSoldiers.add(labelIndexSoldier2);
        m_LablesSoldiers.add(labelIndexSoldier3);
        m_LablesSoldiers.add(labelIndexSoldier4);

    }

    private void initPlayers(List<SinglePlayer> playersToInit, List<SinglePlayer> playersInitiated) throws SnakesAndLaddersRunTimeException {
        SinglePlayer player;
        ePlayerType playertype;
        String playerName;
        int computerIndex = 0;

        for (int i = 0; i < model.getNumOfPlayers(); i++) {
            if (playersToInit == null) {
                playertype = playersInitiated.get(i).getType();
            } else {
                playertype = playersToInit.get(i).getType();
            }
            switch (playertype) {
                case HUMAN:
                    playerName = playersToInit == null ? playersInitiated.get(i).getPlayerName()
                            : playersToInit.get(i).getPlayerName();
                    player = new SinglePlayer(playerName, playertype);
                    player.initSoldiers(model.getCurrGameIndex());
                    model.getCurrGameIndex().getPlayers().add(player);
                    model.addPlayer(player);
                    break;
                case COMPUTER:
                    player = new SinglePlayer("Computer" + computerIndex, playertype);
                    player.initSoldiers(model.getCurrGameIndex());
                    model.getCurrGameIndex().getPlayers().add(player);
                    model.addPlayer(player);
                    computerIndex++;
                    break;
                default:
                    throw new SnakesAndLaddersRunTimeException("InitPlayers(): Invalid PlayerType Input");
            }
        }
    }

    public void InitModel(List<SinglePlayer> playersInitiated) {
        model.initNewGame(shuffleLaddersAndSnakes);
        try {
            initPlayers(null, playersInitiated);
        } catch (SnakesAndLaddersRunTimeException ex) {
            Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.selectFirstPlayer();

        //addition from the controller
        printModelToScene();
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    @FXML
    private void playButtonClicked(ActionEvent event) throws SnakesAndLaddersRunTimeException {
        SinglePlayer player = model.getCurrPlayer();

        cubeAnswer = cube.throwCube();

        if (player.getType() == COMPUTER) {
            makeComputerTurn(player);
//            m_consoleView.LetComputerPlay();
//            m_consoleView.PrintCubeAnswer(cubeAnswer);
//            m_consoleView.PrintPlayer(player);
//            int soldierIndex = player.randomizeCurrentPlayer();
//            m_consoleView.printCurrentSoldier(soldierIndex);
        } else {
            labelCubeAnswer.textProperty().set(String.valueOf(cubeAnswer));
            makeSoldiersAvaliable();
            waitForUserToChooseSoldier();
            //ChooseHumanSoldierToPlay();
            //player.setCurrentSoldier(indexOfSoldier);
        }

//        nextMove = move(player, cubeAnswer);
//
//        return nextMove;
    }

    private void printModelToScene() {
        printCurrPlayerTurn();
        printListOfPlayersWithPictures(model.getPlayers());
        printGameBoard(model.getGame());
    }

    private void printListOfPlayersWithPictures(List<SinglePlayer> players) {

        for (SinglePlayer singlePlayer : players) {
            addPlayerToList(singlePlayer);
        }
    }

    private void printGameBoard(SnakesAndLaddersSingleGame game) {
        BoardView boardView = new BoardView(game.getGameBoard());
        boardPane.getChildren().add(boardView);
    }

    private void printCurrPlayerTurn() {
        SinglePlayer currentPlayer = model.getCurrPlayer();
        labelCurrPlayer.textProperty().set(currentPlayer.getPlayerName() + "'s turn");
        printCurrPlayerSoldiers(currentPlayer);
    }

    private void addPlayerToList(SinglePlayer player) {
        PlayerView playerView = new PlayerView(player.getPlayerName(), player.getType());
        playersPane.getChildren().add(playerView);
    }

    @FXML
    private void newGameSelected(ActionEvent event) {
        selectedNewGame.set(true);
    }

    @FXML
    private void openSelected(ActionEvent event) {
        openGameSelected.set(true);
    }

    @FXML
    private void closeGameSelected(ActionEvent event) {
        closeGame.set(true);
    }

    @FXML
    private void saveGameSelected(ActionEvent event) {
        saveGameSelected.set(true);
    }

    @FXML
    private void saveAsGameSelected(ActionEvent event) {
        saveGameAsSelected.set(true);
    }

    @FXML
    private void QuitGameSelected(ActionEvent event) {
        quitGameSelected.set(true);
    }

    @FXML
    private void HelpSelected(ActionEvent event) {
        helpSelected.set(true);
    }

    public SimpleBooleanProperty getQuitGameSelected() {
        return quitGameSelected;
    }

    public SimpleBooleanProperty getSelectedNewGame() {
        return selectedNewGame;
    }

    public SimpleBooleanProperty getOpenGameSelected() {
        return openGameSelected;
    }

    public SimpleBooleanProperty getSaveGameAsSelected() {
        return saveGameAsSelected;
    }

    private void printCurrPlayerSoldiers(SinglePlayer currentPlayer) {
        int colorNumber = currentPlayer.getColor();
        List<Soldier> currentSoldiers = currentPlayer.getM_SoldiersList();

        for (int i = 0; i < currentSoldiers.size(); i++) {
            m_LablesSoldiers.get(i).setText(String.valueOf(
                    currentSoldiers.get(i).getLocationOnBoard().getSquareNumber()));
        }

        switch (colorNumber) {
            case 1:
                Image imageBlue = ImageManager.getImage("BluePlayer");
                for (ImageView imageView : m_ImageViewSoldiers) {
                    imageView.setImage(imageBlue);
                    imageView.disableProperty().set(true);
                }
                break;
            case 2:
                Image imageGreen = ImageManager.getImage("GreenPlayer");
                for (ImageView imageView : m_ImageViewSoldiers) {
                    imageView.setImage(imageGreen);
                    imageView.disableProperty().set(true);
                }
                break;
            case 3:
                Image imagePurple = ImageManager.getImage("PurplePlayer");
                for (ImageView imageView : m_ImageViewSoldiers) {
                    imageView.setImage(imagePurple);
                    imageView.disableProperty().set(true);
                }
                break;
            case 4:
                Image imageYellow = ImageManager.getImage("YellowPlayer");
                for (ImageView imageView : m_ImageViewSoldiers) {
                    imageView.setImage(imageYellow);
                    imageView.disableProperty().set(true);
                }
                break;
        }
    }

    private void makeComputerTurn(SinglePlayer player) {
    }

    private void makeSoldiersAvaliable() {
        for (ImageView imageSoldier : m_ImageViewSoldiers) {
            imageSoldier.disableProperty().set(false);
        }
    }

    private void waitForUserToChooseSoldier() {

        //while (!newValue) {
        userChooseSoldier1.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    userChooseSoldier1.set(false);
                    soldierChoosed(1);
                }
            }
        });

        userChooseSoldier2.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    userChooseSoldier2.set(false);
                    soldierChoosed(2);
                }
            }
        });

        userChooseSoldier3.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    userChooseSoldier3.set(false);
                    soldierChoosed(3);
                }
            }
        });

        userChooseSoldier4.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    userChooseSoldier4.set(false);
                    soldierChoosed(4);
                }
            }
        });
    }

    private void soldierChoosed(int soldierNumber) {
        disableSoldierImages();
        playWithSoldier(soldierNumber);
    }

    @FXML
    private void ChooseSoldier1(MouseEvent event) {
        userChooseSoldier1.set(true);
    }

    @FXML
    private void ChooseSoldier2(MouseEvent event) {
        userChooseSoldier2.set(true);
    }

    @FXML
    private void ChooseSoldier3(MouseEvent event) {
        userChooseSoldier3.set(true);
    }

    @FXML
    private void ChooseSoldier4(MouseEvent event) {
        userChooseSoldier4.set(true);
    }

    private void disableSoldierImages() {
        for (ImageView imageSoldier : m_ImageViewSoldiers) {
            imageSoldier.disableProperty().set(true);
        }
    }

    private void playWithSoldier(int soldierNumber) {
        //TODO: logic of playing
        //TODO: maby we can use soldier as custon image that have number and just printing it - polymorphism
        switch (soldierNumber) {
            case 1:
                labelIndexSoldier1.textProperty().set(
                        (String.valueOf(Integer.valueOf(labelIndexSoldier1.getText()) + cubeAnswer)));
                break;
            case 2:
                labelIndexSoldier2.textProperty().set(
                        (String.valueOf(Integer.valueOf(labelIndexSoldier2.getText()) + cubeAnswer)));
                break;
            case 3:
                labelIndexSoldier3.textProperty().set(
                        (String.valueOf(Integer.valueOf(labelIndexSoldier3.getText()) + cubeAnswer)));
                break;
            case 4:
                labelIndexSoldier4.textProperty().set(
                        (String.valueOf(Integer.valueOf(labelIndexSoldier4.getText()) + cubeAnswer)));
                break;
        }
    }

    public Node getSquareView(int squareNumber) {

        GridPane boardview = (GridPane) boardPane.getChildren().get(0);
        ObservableList<Node> squareViews = boardview.getChildren();

        if (squareNumber < 1 || squareNumber > model.getGame().getMAX_SQUARE_NUM()) {
            return null;
        }
        int boardSize = model.getGame().getO_BoardSize();
        int x = (boardSize * boardSize - 1 - (squareNumber - 1)) / boardSize;
        int y = (squareNumber - 1) % boardSize;

        for (Node node : squareViews) {
            if (node.isManaged()) {
                if (boardview.getRowIndex(node) == x && boardview.getColumnIndex(node) == y) {
                    return node;
                }
            }
        }
        return null;
    }
}
