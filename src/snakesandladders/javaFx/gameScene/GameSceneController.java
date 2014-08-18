/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.gameScene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;
import snakesandladders.javaFx.components.BoardView;
import snakesandladders.javaFx.components.ImageManager;
import snakesandladders.javaFx.components.PlayerView;
import snakesandladders.javaFx.initScene.SceneInitController;
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.ePlayerType;

/**
 * FXML Controller class
 *
 * @author Noam
 */
public class GameSceneController implements Initializable {

    boolean shuffleLaddersAndSnakes = true;

    @FXML
    private Font x1;
    @FXML
    private Color x2;
    @FXML
    private Font x3;
    private GameModel model;
    private SceneInitController sceneInitController;
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

    private void initPlayers(List<SinglePlayer> players) throws SnakesAndLaddersRunTimeException {
        SinglePlayer player;
        ePlayerType playertype;
        String playerName;
        int computerIndex = 0;

        for (int i = 0; i < model.getNumOfPlayers(); i++) {
            if (players == null) {
                playertype = sceneInitController.getPlayerType(i);
            } else {
                playertype = players.get(i).getType();
            }
            switch (playertype) {
                case HUMAN:
                    playerName = players == null ? sceneInitController.getPlayerString(i)
                            : players.get(i).getPlayerName();
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

    public void InitModel() {
        model.initNewGame(shuffleLaddersAndSnakes);
        try {
            initPlayers(null);
        } catch (SnakesAndLaddersRunTimeException ex) {
            Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.selectFirstPlayer();

        //addition from the controller
        printModelToScene();
    }

    public void setModelAndInitController(GameModel model, SceneInitController sceneInitController) {
        this.model = model;
        this.sceneInitController = sceneInitController;
    }

    @FXML
    private void playButtonClicked(ActionEvent event) {
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
        BoardView boardView = new BoardView(game.getO_BoardSize());
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
                }
                break;
            case 2:
                Image imageGreen = ImageManager.getImage("GreenPlayer");
                for (ImageView imageView : m_ImageViewSoldiers) {
                    imageView.setImage(imageGreen);
                }
                break;
            case 3:
                Image imagePurple = ImageManager.getImage("PurplePlayer");
                for (ImageView imageView : m_ImageViewSoldiers) {
                    imageView.setImage(imagePurple);
                }
                break;
            case 4:
                Image imageYellow = ImageManager.getImage("YellowPlayer");
                for (ImageView imageView : m_ImageViewSoldiers) {
                    imageView.setImage(imageYellow);
                }
                break;
        }
    }

}
