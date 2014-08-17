/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.gameScene;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.javaFx.initScene.SceneInitController;
import snakesandladders.players.SinglePlayer;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        model.initNewGame(shuffleLaddersAndSnakes);
//        try {
//            initPlayers(null);
//        } catch (SnakesAndLaddersRunTimeException ex) {
//            Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        model.selectFirstPlayer();

    }
    
    private void initPlayers(List<SinglePlayer> players) throws SnakesAndLaddersRunTimeException {
//        SinglePlayer player;
//        ePlayerType playertype;
//        String playerName;
//        int computerIndex = 0;
//
//        for (int i = 0; i < model.getNumOfPlayers(); i++) {
//            if (players == null) {
//                playertype = sceneInitController.getPlayerType(i);
//            } else {
//                playertype = players.get(i).getType();
//            }
//            switch (playertype) {
//                case HUMAN:
//                    playerName = players == null ? sceneInitController.getPlayerString(model.getPlayers())
//                            : players.get(i).getPlayerName();
//                    player = new SinglePlayer(playerName, playertype);
//                    player.initSoldiers(model.getCurrGameIndex());
//                    model.getCurrGameIndex().getPlayers().add(player);
//                    model.addPlayer(player);
//                    break;
//                case COMPUTER:
//                    player = new SinglePlayer("Computer" + computerIndex, playertype);
//                    player.initSoldiers(model.getCurrGameIndex());
//                    model.getCurrGameIndex().getPlayers().add(player);
//                    model.addPlayer(player);
//                    computerIndex++;
//                    break;
//                default:
//                    throw new SnakesAndLaddersRunTimeException("InitPlayers(): Invalid PlayerType Input");
//            }
//        }
    }

    public void setModelAndInitController(GameModel model, SceneInitController sceneInitController) {
        this.model = model;
        this.sceneInitController = sceneInitController;
    }

}
