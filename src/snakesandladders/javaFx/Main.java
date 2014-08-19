/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.CollationElementIterator;
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
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import snakesandladders.gamecontrol.eStartMenu;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.javaFx.gameScene.GameSceneController;
import snakesandladders.javaFx.initScene.SceneInitController;
import snakesandladders.javaFx.utils.dialog.CustomizablePromptDialog;
import snakesandladders.players.SinglePlayer;
import snakesandladders.xml.XML;
import snakesandladders.xml.eXMLLoadStatus;

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

                    gameSceneController.InitModel();

                    lisionersForGame(gameSceneController, sceneInitController, primaryStage);

                    javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    Scene scene = new Scene(rootGame, screenBounds.getWidth(), screenBounds.getHeight());

                    primaryStage.setX(
                            0);
                    primaryStage.setY(
                            0);
                    primaryStage.setTitle(
                            "Game");

                    primaryStage.setScene(scene);

                    primaryStage.show();
                }
            }
        });

        sceneInitController.getCancelInit().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    primaryStage.close();
                }
            }
        });
        return sceneInitController;
    }

    private void lisionersForGame(GameSceneController gameSceneController, final SceneInitController sceneInitController, final Stage primaryStage) {

        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent window) {
                if (!getFinalAnswer(primaryStage)) {
                    window.consume();
                }
            }
        });

        gameSceneController.getQuitGameSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    getFinalAnswer(primaryStage);
                }
            }
        });

        gameSceneController.getSelectedNewGame().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        start(primaryStage);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        gameSceneController.getSelectedNewGame().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        start(primaryStage);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        gameSceneController.getOpenGameSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        openXML(primaryStage, sceneInitController); //TODO: check returned value
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        gameSceneController.getSaveGameAsSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    saveXML(primaryStage);
                }
            }

        });

    }

    private boolean getFinalAnswer(Stage primaryStage) {
        boolean quit = true;
        String answer = CustomizablePromptDialog.show(primaryStage, "What do you want do to?", "Quit without saving", "Save and quit", "Stay");
        switch (answer) {
            case ("Quit without saving"):
                primaryStage.close();
                break;
            case ("Save and quit"):
                primaryStage.close();   //TODO: implement save
                break;
            case ("Stay"):
                quit = false;
                break;
        }
        return quit;
    }

    private eXMLLoadStatus openXML(Stage stage, SceneInitController initSceneController) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );

        fileChooser.setTitle("Open XML File");
        eXMLLoadStatus status = loadGame(fileChooser.showOpenDialog(stage));
        if (status != eXMLLoadStatus.LOAD_SUCCESS) {
            return status;
        }

        startWithInitializedModel(stage);
        return status.LOAD_SUCCESS;
    }

    private void saveXML(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );

        fileChooser.setTitle("Save XML File");
        fileChooser.showSaveDialog(stage);
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

    private eXMLLoadStatus loadGame(File xmlFile) throws IOException {
        eXMLLoadStatus loadStatus;
        SinglePlayer.setNextId(0);
        GameModel modelLoad = null;
        String xmlPath = xmlFile.getCanonicalPath();

        loadStatus = XML.initModelFromXml(xmlPath, modelLoad);
        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            //GameSceneController.displayXMLLoadError(loadStatus);
            return loadStatus;
        }
        modelLoad = new GameModel(XML.getM_GameSize(), XML.getM_NumOfSnakesAndLadders(), XML.getM_NumOfPlayers(), XML.getM_NumOfSoldiersToWin());
        loadStatus = XML.loadXML(xmlPath, modelLoad);

        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            //GameSceneController.displayXMLLoadError(loadStatus);
            return loadStatus;
        }

        model = modelLoad;

        return loadStatus;
    }

    private void startWithInitializedModel(Stage primaryStage) {

    }
}
