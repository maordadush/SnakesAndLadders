/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.javaFx.gameScene.GameSceneController;
import snakesandladders.javaFx.initScene.SceneInitController;
import snakesandladders.players.SinglePlayer;

/**
 *
 * @author Noam
 */
public class Main extends Application {

    private static final String INIT_SCENE_FXML_PATH = "initScene/SceneInit.fxml";
    private static final String GAME_SCENE_FXML_PATH = "gameScene/gameScene.fxml";

    private GameModel model;

    @Override
    public void start(final Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = getFXMLLoader(INIT_SCENE_FXML_PATH);
        Parent playersRoot = getRoot(fxmlLoader);
        SceneInitController playersController = getPlayersController(fxmlLoader, primaryStage);

        Scene scene = new Scene(playersRoot, 600, 600);

        primaryStage.setTitle("Start new game");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private SceneInitController getPlayersController(final FXMLLoader fxmlLoader, final Stage primaryStage) {
        final SceneInitController sceneInitController = (SceneInitController) fxmlLoader.getController();
        sceneInitController.setModel(model);

        sceneInitController.getFinishedInit().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    SinglePlayer.setNextId(0);
                    model = new GameModel(sceneInitController.getBoardSize(), sceneInitController.GetNumOfSnakesAndLadders(),
                            sceneInitController.GetNumOfPlayers(), sceneInitController.getNumberOfSoldiersToWin());
                    FXMLLoader fxmlLoader = getFXMLLoader(GAME_SCENE_FXML_PATH);

                    Parent rootGame;
                    try {
                        rootGame = getRoot(fxmlLoader);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        rootGame = null;
                    }
                    GameSceneController gameSceneController = (GameSceneController) fxmlLoader.getController();
                    gameSceneController.setModelAndInitController(model, sceneInitController);

                    javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    Scene scene = new Scene(rootGame, screenBounds.getWidth(), screenBounds.getHeight());
                    primaryStage.setX(0);
                    primaryStage.setY(0);
                    primaryStage.setTitle("Game");

                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            }
        });
        return sceneInitController;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private FXMLLoader getFXMLLoader(String URLPath) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(URLPath);
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }

    private Parent getRoot(FXMLLoader fxmlLoader) throws IOException {
        return (Parent) fxmlLoader.load(fxmlLoader.getLocation().openStream());
    }
}
