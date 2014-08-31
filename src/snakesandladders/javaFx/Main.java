/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.javaFx.gameScene.GameSceneController;
import snakesandladders.javaFx.initScene.SceneInitController;
import snakesandladders.javaFx.utils.dialog.CustomizablePromptDialog;
import snakesandladders.players.SinglePlayer;
import snakesandladders.xml.XML;
import snakesandladders.xml.XMLException;
import snakesandladders.xml.eXMLLoadStatus;
import snakesandladders.xml.eXMLSaveStatus;

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
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Start new game");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private SceneInitController getPlayersController(final FXMLLoader fxmlLoader, final Stage primaryStage) {
        final SceneInitController sceneInitController = (SceneInitController) fxmlLoader.getController();

        sceneInitController.getFinishedInit().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    List<SinglePlayer> playersInitiated = createPlayerListFromSceneInit(sceneInitController);
                    SinglePlayer.setNextId(0);
                    model = new GameModel(sceneInitController.getBoardSize(), sceneInitController.GetNumOfSnakesAndLadders(),
                            sceneInitController.GetNumOfPlayers(), sceneInitController.getNumberOfSoldiersToWin());
                    FXMLLoader fxmlLoader = getFXMLLoader(GAME_SCENE_FXML_PATH);

                    Parent rootGame;
                    try {
                        rootGame = getRoot(fxmlLoader);
                    } catch (IOException ex) {
                        sceneInitController.showError(ex.getMessage());
                        rootGame = null;
                    }

                    GameSceneController gameSceneController = (GameSceneController) fxmlLoader.getController();

                    gameSceneController.setModel(model);

                    try {
                        gameSceneController.InitModel(true, playersInitiated);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        sceneInitController.showError(ex.getMessage());
                    }

                    listenersForGame(gameSceneController, primaryStage, rootGame);

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    Scene scene = new Scene(rootGame, screenBounds.getWidth(), screenBounds.getHeight());

                    primaryStage.setX(0);
                    primaryStage.setY(0);

                    primaryStage.setTitle(
                            model.getM_GameName());

                    primaryStage.setScene(scene);

                    primaryStage.show();
                }
            }
        });

        sceneInitController.getOpenGameSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        Parent rootGame = null;
                        GameSceneController gameSceneController = null;
                        sceneInitController.getOpenGameSelected().set(false);
                        eXMLLoadStatus returnedValue = openXML(primaryStage, gameSceneController, rootGame);
                      //  gameSceneController.displayMessage(returnedValue.name());
                    } catch (IOException ex) {
                        sceneInitController.showError(ex.getMessage());
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        sceneInitController.showError(ex.getMessage());
                    }
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

    List<SinglePlayer> createPlayerListFromSceneInit(SceneInitController sceneInitController) {
        List<SinglePlayer> listToReturn = new ArrayList<>();

        for (int i = 0; i < sceneInitController.GetNumOfPlayers(); i++) {
            listToReturn.add(new SinglePlayer(sceneInitController.getLegalPlayerString(),
                    sceneInitController.getLegalPlayerType()));
        }

        return listToReturn;
    }

    private void listenersForGame(final GameSceneController gameSceneController, final Stage primaryStage, final Parent rootGame) {

        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent window) {
                try {
                    if (!getFinalAnswer(primaryStage)) {
                        window.consume();
                    } else {
                        System.exit(1);
                    }
                } catch (XMLException ex) {
                    gameSceneController.displayMessage(ex.getMessage());
                }
            }
        });

        gameSceneController.getQuitGameSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    gameSceneController.getQuitGameSelected().set(false);
                    try {
                        getFinalAnswer(primaryStage);
                    } catch (XMLException ex) {
                        gameSceneController.displayMessage(ex.getMessage());
                    }
                }
            }
        });

        gameSceneController.getSelectedNewGame().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        gameSceneController.getSelectedNewGame().set(false);
                        primaryStage.close();
                        start(primaryStage);
                    } catch (IOException ex) {
                        gameSceneController.displayMessage("Cannot start new game");
                    }
                }
            }
        });

        gameSceneController.getOpenGameSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        gameSceneController.getOpenGameSelected().set(false);
                        eXMLLoadStatus loadStatus = openXML(primaryStage, gameSceneController, rootGame);
                        gameSceneController.displayMessage(loadStatus.name());
                    } catch (IOException ex) {
                        gameSceneController.displayMessage(ex.getMessage());
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        gameSceneController.displayMessage(ex.getMessage());
                    }
                }
            }
        });

        gameSceneController.getSaveGameAsSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    gameSceneController.getSaveGameAsSelected().set(false);
                    try {
                        saveXML(primaryStage);
                    } catch (XMLException ex) {
                        gameSceneController.displayMessage(ex.getMessage());
                    }
                }
            }

        });

        gameSceneController.getSaveGameSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    gameSceneController.getSaveGameSelected().set(false);
                    if (model.getSaveGamePath() == null) {
                        try {
                            saveXML(primaryStage);
                        } catch (XMLException ex) {
                            gameSceneController.displayMessage(eXMLSaveStatus.COULD_NOT_CREATE_FILE.name());
                        }
                    } else {
                        eXMLSaveStatus saveStatus;
                        saveStatus = XML.saveXML(model.getSaveGamePath(), model);
                        if (saveStatus != eXMLSaveStatus.SAVE_SUCCESS) {
                            gameSceneController.displayMessage(saveStatus.name());
                        } else {
                            gameSceneController.displayXMLSavedSuccessfully(model.getSaveGamePath());
                        }
                    }
                }
            }

        });

    }

    private boolean getFinalAnswer(Stage primaryStage) throws XMLException {
        boolean quit = true;
        String answer = CustomizablePromptDialog.show(primaryStage, "What do you want do to?","Quit", "Stay");
        switch (answer) {
            case ("Quit"):
                System.exit(0);
                break;
            case ("Stay"):
                quit = false;
                break;
        }
        return quit;
    }

    private eXMLLoadStatus openXML(Stage stage, GameSceneController gameSceneController, Parent rootGame) throws IOException, SnakesAndLaddersRunTimeException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );

        fileChooser.setTitle("Open XML File");
        eXMLLoadStatus status = loadGame(fileChooser.showOpenDialog(stage));
        if (status != eXMLLoadStatus.LOAD_SUCCESS) {
            return status;
        }

        startWithInitializedModel(stage, gameSceneController, rootGame);
        return status.LOAD_SUCCESS;
    }

    private void saveXML(Stage stage) throws XMLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );

        fileChooser.setTitle("Save XML File");
        String gamePath = fileChooser.showSaveDialog(stage).getAbsolutePath();
        eXMLSaveStatus saveStatus = XML.saveXML(gamePath, model);
        if (saveStatus != eXMLSaveStatus.SAVE_SUCCESS) {
            throw new XMLException("Save Failed");
        } else {
            model.setSaveGamePath(gamePath);
        }

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
            return loadStatus;
        }
        modelLoad = new GameModel(XML.getM_GameSize(), XML.getM_NumOfSnakesAndLadders(), XML.getM_NumOfPlayers(), XML.getM_NumOfSoldiersToWin());
        loadStatus = XML.loadXML(xmlPath, modelLoad);

        if (loadStatus != eXMLLoadStatus.LOAD_SUCCESS) {
            return loadStatus;
        }

        model = modelLoad;

        return loadStatus;
    }

    private void startWithInitializedModel(Stage primaryStage, GameSceneController gameSceneController, Parent rootGame) throws SnakesAndLaddersRunTimeException, IOException {

        SinglePlayer.setNextId(0);
        FXMLLoader fxmlLoader = getFXMLLoader(GAME_SCENE_FXML_PATH);
        rootGame = getRoot(fxmlLoader);

        gameSceneController = (GameSceneController) fxmlLoader.getController();

        gameSceneController.setModel(model);
        gameSceneController.InitModel(false, model.getPlayers());

        listenersForGame(gameSceneController, primaryStage, rootGame);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(rootGame, screenBounds.getWidth(), screenBounds.getHeight());

        primaryStage.setX(0);
        primaryStage.setY(0);

        primaryStage.setTitle(
                model.getM_GameName());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

}
