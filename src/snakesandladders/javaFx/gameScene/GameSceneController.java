/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.gameScene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import snakesandladders.exception.SnakesAndLaddersRunTimeException;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.Cube;
import snakesandladders.gamemodel.GameModel;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;
import snakesandladders.gamemodel.eChars;
import snakesandladders.javaFx.components.BoardView;
import snakesandladders.javaFx.components.ImageManager;
import snakesandladders.javaFx.components.PlayerView;
import snakesandladders.javaFx.components.SquareView;
import snakesandladders.javaFx.utils.SnakesAndLaddersDrawingUtil;
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;
import snakesandladders.players.ePlayerType;
import static snakesandladders.players.ePlayerType.COMPUTER;

/**
 * FXML Controller class
 *
 * @author Noam
 */
public class GameSceneController implements Initializable {

    boolean startNewGame = true;
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
    BoardView boardView;
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
    SnakesAndLaddersDrawingUtil snlUtil;
    private List<Image> cubeImages;

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
    private Label labelNotification;
    @FXML
    private ImageView imageCubeAnswer;
    @FXML
    private ScrollPane scrollPaneMiddle;
    @FXML
    private BorderPane boarderPaneRight;
    @FXML
    private AnchorPane anchorPaneLeft;
    private String paneStyle = "-fx-background-color: #ffffe0;";
    private TranslateTransition transitionMove;
    private MoveTransition moveTransition;

    private boolean isErrorMessageShown;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        playersPane.setSpacing(10);
        isErrorMessageShown = false;
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
        cubeImages = new ArrayList<>();
        loadImagesToList(cubeImages);

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

    public MoveTransition getMoveTransition() {
        return moveTransition;
    }

    private void initPlayers(List<SinglePlayer> playersToInit) throws SnakesAndLaddersRunTimeException {
        SinglePlayer player;
        ePlayerType playertype;
        String playerName;
        int computerIndex = 0;

        for (int i = 0; i < model.getNumOfPlayers(); i++) {
            playertype = playersToInit.get(i).getType();
            switch (playertype) {
                case HUMAN:
                    playerName = playersToInit.get(i).getPlayerName();
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

    public void InitModel(Boolean startNewGame, List<SinglePlayer> playersInitiated) throws SnakesAndLaddersRunTimeException {
        List<SinglePlayer> players = new ArrayList<>(playersInitiated);

        try {
            if (startNewGame) {
                model.initNewGame(startNewGame);
                initPlayers(players);
            } else {
                model.setPlayers(players);
            }

        } catch (SnakesAndLaddersRunTimeException ex) {
            Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.selectFirstPlayer();

        //addition from the controller
        printModelToScene();
        if (model.getCurrPlayer().getType() == COMPUTER) {
            makeComputerTurn(model.getCurrPlayer());
        }

    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    @FXML
    private void playButtonClicked(ActionEvent event) throws SnakesAndLaddersRunTimeException {
        hideError();
        buttonPlay.disableProperty().set(true);
        SinglePlayer player = model.getCurrPlayer();
        cubeAnswer = cube.throwCube();
        showCubeAnswer(cubeAnswer);

        if (player.getType() == COMPUTER) {
            makeComputerTurn(player);
        } else {
            makeSoldiersAvaliable();
            waitForUserToChooseSoldier();
        }
    }

    private void printModelToScene() {

        printGameBoard(model.getGame());
        printSnakeAndLadders(model.getGame());
        printPlayersSoldiers();
        setPlayerTurn();
        printListOfPlayersWithPictures(model.getPlayers());

    }

    private void printListOfPlayersWithPictures(List<SinglePlayer> players) {

        for (SinglePlayer singlePlayer : players) {
            addPlayerToList(singlePlayer);
        }
    }

    private void printGameBoard(SnakesAndLaddersSingleGame game) {
        boardView = new BoardView(game.getGameBoard());
        moveTransition = new MoveTransition(boardView.getWidth(), boardView.getHeight(), boardView);
        boardPane.setStyle(paneStyle);
        boardView.getChildren().add(moveTransition);
        boardPane.getChildren().add(boardView);

    }

    private void setPlayerTurn() {
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

    public SimpleBooleanProperty getSaveGameSelected() {
        return saveGameSelected;
    }

    private void printCurrPlayerSoldiers(SinglePlayer currentPlayer) {
        int colorNumber = currentPlayer.getColor();
        Image soldierImage = getImageSoldier(colorNumber);

        List<Soldier> currentSoldiers = currentPlayer.getM_SoldiersList();

        for (int i = 0; i < currentSoldiers.size(); i++) {
            int squareNumber = currentSoldiers.get(i).getLocationOnBoard().getSquareNumber();
            int numOfSoldiers = currentPlayer.getNumSoldiersAtSquare(currentSoldiers.get(i).getLocationOnBoard());
            SquareView node = (SquareView) getSquareView(squareNumber);
            node.addSoldier(currentPlayer.getPlayerID(), soldierImage, numOfSoldiers);
            m_LablesSoldiers.get(i).setText(String.valueOf(squareNumber));
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

    Image getImageSoldier(int colorNumber) {
        switch (colorNumber) {
            case 1:
                Image imageBlue = ImageManager.getImage("BluePlayer");
                return imageBlue;
            case 2:
                Image imageGreen = ImageManager.getImage("GreenPlayer");
                return imageGreen;
            case 3:
                Image imagePurple = ImageManager.getImage("PurplePlayer");
                return imagePurple;
            case 4:
                Image imageYellow = ImageManager.getImage("YellowPlayer");
                return imageYellow;
        }
        return null;
    }

    void makeComputerTurn(SinglePlayer player) throws SnakesAndLaddersRunTimeException {
        cubeAnswer = cube.throwCube();
        showCubeAnswer(cubeAnswer);
        int soldierIndex = getRandomSoldierIndex(player);
        player.setCurrentSoldier(soldierIndex);
        soldierChoosed(player);
    }

    private void makeSoldiersAvaliable() {
        for (ImageView imageSoldier : m_ImageViewSoldiers) {
            imageSoldier.disableProperty().set(false);

        }
    }

    private void waitForUserToChooseSoldier() {

//        //while (!newValue) {
        userChooseSoldier1.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    SinglePlayer player = model.getCurrPlayer();

                    userChooseSoldier1.set(false);
                    try {
                        player.setCurrentSoldier(1);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        soldierChoosed(player);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        userChooseSoldier2.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    final SinglePlayer player = model.getCurrPlayer();

                    userChooseSoldier2.set(false);
                    try {
                        player.setCurrentSoldier(2);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        soldierChoosed(player);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        userChooseSoldier3.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    final SinglePlayer player = model.getCurrPlayer();

                    userChooseSoldier3.set(false);
                    try {
                        player.setCurrentSoldier(3);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        soldierChoosed(player);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        userChooseSoldier4.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    final SinglePlayer player = model.getCurrPlayer();

                    userChooseSoldier4.set(false);
                    try {
                        player.setCurrentSoldier(4);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        soldierChoosed(player);
                    } catch (SnakesAndLaddersRunTimeException ex) {
                        Logger.getLogger(GameSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void soldierChoosed(SinglePlayer player) throws SnakesAndLaddersRunTimeException {
        boolean finishedGame = false;
        disableSoldierImages();
        playWithSoldier(player);
        finishedGame = checkPlayerWon(player);

        if (finishedGame) {
            disableInnerPanes();
            alertPlayerWon(player);
        }

        buttonPlay.disableProperty().set(false);
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

    private void playWithSoldier(SinglePlayer player) throws SnakesAndLaddersRunTimeException {
        Soldier currSoldier = player.getCurrentSoldier();
        BoardSquare currSquare = currSoldier.getLocationOnBoard();
        SquareView origin = (SquareView) getSquareView(currSquare.getSquareNumber());
        BoardSquare toMove = move(player, cubeAnswer);
        SquareView dest = (SquareView) getSquareView(toMove.getSquareNumber());

        origin.removeSoldier(player.getPlayerID(), getImageSoldier(player.getColor()), player.getNumSoldiersAtSquare(currSquare));

        updateSoldierIfWon(currSoldier, currSquare);

        model.forwardPlayer();
        setPlayerTurn();

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

    private BoardSquare move(SinglePlayer player, int cubeAnswer) throws SnakesAndLaddersRunTimeException {
        Soldier currentSoldier = player.GetCurrentSoldier();
        BoardSquare originSquare = currentSoldier.getLocationOnBoard();
        SquareView origin = (SquareView) getSquareView(originSquare.getSquareNumber());
        BoardSquare boardToMove;
        int oldPlyerIndex = originSquare.getSquareNumber();
        int newPlayerIndex = oldPlyerIndex + cubeAnswer;

        if (newPlayerIndex < (model.getGame().getMAX_SQUARE_NUM())) {
            boardToMove = model.getGame().getBoardSquare(newPlayerIndex);
        } else {
            boardToMove = model.getGame().getBoardSquare(model.getGame().getMAX_SQUARE_NUM());
            currentSoldier.setM_FinishedGame(true);
        }
        BoardSquare originBoardToMove = boardToMove;
        SquareView originSquareView = (SquareView) getSquareView(originBoardToMove.getSquareNumber());

        SquareView middle = (SquareView) getSquareView(boardToMove.getSquareNumber());
        moveTransition.moveSoldier(Integer.valueOf(origin.getId()), Integer.valueOf(middle.getId()), getImageSoldier(player.getColor()),
                player, middle, boardToMove, model, this);
        switch (boardToMove.getType()) {
            case LADDER_TAIL:
                boardToMove = boardToMove.getJumpTo();
                break;
            case SNAKE_HEAD:
                boardToMove = boardToMove.getJumpTo();
                break;
            case NONE:
                break;
        }
        if (!boardToMove.getPlayers().contains(player)) {
            boardToMove.getPlayers().add(player);
        }
        model.setMove(player, boardToMove);
        //check if exist soldiers in origin square
        if (!player.atSquare(originSquare)) {
            originSquare.getPlayers().remove(player);
        }
        SquareView dest = (SquareView) getSquareView(boardToMove.getSquareNumber());
        if (middle != dest) {
            middle.removeSoldier(player.getPlayerID(), getImageSoldier(player.getColor()), player.getNumSoldiersAtSquare(originBoardToMove));
            moveTransition.moveSoldier(Integer.valueOf(middle.getId()), Integer.valueOf(dest.getId()), getImageSoldier(player.getColor()),
                    player, dest, boardToMove, model, this);
        }
        return boardToMove;
    }

    private void printPlayersSoldiers() {
        for (SinglePlayer player : model.getPlayers()) {
            printCurrPlayerSoldiers(player);
        }
    }

    private boolean checkPlayerWon(SinglePlayer player) {
        boolean returnedValue = false;
        int finishedSoldier = 0;

        for (Soldier soldier : player.getM_SoldiersList()) {
            if (soldier.isM_FinishedGame()) {
                finishedSoldier++;
            }
        }
        if (finishedSoldier >= model.getM_NumOfSoldiersToWin()) {
            returnedValue = true;
        }

        return returnedValue;
    }

    private void alertPlayerWon(SinglePlayer player) {
        labelNotification.textProperty().set(player.getPlayerName() + " won! Game Over!");
    }

    private void updateSoldierIfWon(Soldier currSoldier, BoardSquare currSquare) {
        if (currSquare.getSquareNumber() >= model.getGame().getMAX_SQUARE_NUM()) {
            currSoldier.setM_FinishedGame(true);
        }
    }

    private int getRandomSoldierIndex(SinglePlayer player) {
        int randomSoldierIndex;
        do {
            Random rand = new Random();
            randomSoldierIndex = rand.nextInt(3) + 1;
        } while (player.getM_SoldiersList().get(randomSoldierIndex).isM_FinishedGame());

        return randomSoldierIndex;
    }

    private void showCubeAnswer(int cubeAnswer) {
        imageCubeAnswer.setImage(cubeImages.get(cubeAnswer - 1));
    }

    private void loadImagesToList(List<Image> cubeImages) {
        Image imageOne = ImageManager.getImage("cubeAnswers/1");
        Image imageTwo = ImageManager.getImage("cubeAnswers/2");
        Image imageThree = ImageManager.getImage("cubeAnswers/3");
        Image imageFour = ImageManager.getImage("cubeAnswers/4");
        Image imageFive = ImageManager.getImage("cubeAnswers/5");
        Image imageSix = ImageManager.getImage("cubeAnswers/6");

        cubeImages.add(imageOne);
        cubeImages.add(imageTwo);
        cubeImages.add(imageThree);
        cubeImages.add(imageFour);
        cubeImages.add(imageFive);
        cubeImages.add(imageSix);
    }

    private void disableInnerPanes() {
        anchorPaneLeft.visibleProperty().set(false);
        scrollPaneMiddle.visibleProperty().set(false);
        boarderPaneRight.visibleProperty().set(false);
    }

    public void displayMessage(String error) {
        if (!isErrorMessageShown) {
            isErrorMessageShown = true;
            labelNotification.textProperty().setValue(error);
            FadeTransition animation = FadeTransitionBuilder.create()
                    .node(labelNotification)
                    .duration(Duration.seconds(0.3))
                    .fromValue(0.0)
                    .toValue(1.0)
                    .build();
            animation.play();
        }
    }

    public void hideError() {
        if (isErrorMessageShown) {
            FadeTransition animation = FadeTransitionBuilder.create()
                    .node(labelNotification)
                    .duration(Duration.seconds(0.3))
                    .fromValue(1.0)
                    .toValue(0.0)
                    .build();
            animation.play();

            isErrorMessageShown = false;
            labelNotification.textProperty().setValue("");

        }
    }

    public void displayXMLSavedSuccessfully(String saveGamePath) {
        labelNotification.textProperty().set("Game saved successfully to: " + saveGamePath);

    }

    private void printSnakeAndLadders(SnakesAndLaddersSingleGame game) {
        int boradSize = game.getO_BoardSize();
        snlUtil = new SnakesAndLaddersDrawingUtil(boradSize, boardView, boardPane);
        for (int i = boradSize - 1; i >= 0; i--) {
            for (int j = 0; j < boradSize; j++) {
                BoardSquare bs = game.getBoardSquare(i, j);
                eChars bsType = bs.getType();
                if (bsType == eChars.LADDER_TAIL) {
                    snlUtil.addLadderOrSnake(game.getBoardSquareX(bs.getSquareNumber()), game.getBoardSquareY(bs.getSquareNumber()), game.getBoardSquareX(bs.getJumpTo().getSquareNumber()),
                            game.getBoardSquareY(bs.getJumpTo().getSquareNumber()), true);
                } else if (bsType == eChars.SNAKE_HEAD) {
                    snlUtil.addLadderOrSnake(game.getBoardSquareX(bs.getSquareNumber()), game.getBoardSquareY(bs.getSquareNumber()),
                            game.getBoardSquareX(bs.getJumpTo().getSquareNumber()),
                            game.getBoardSquareY(bs.getJumpTo().getSquareNumber()), false);

                }

            }

        }
    }

    private TranslateTransition createTransitionMove(int playerID, Image imageSoldier, int numSoldiersAtSquare, SquareView origin, SquareView dest) {
        ImageView imageView = origin.getSoldierImage(playerID);

        //ImageView imageView = new ImageView();
        //imageView.setImage(imageSoldier);
        //  boardPane.getChildren().add(imageView);
//        double boardViewX = boardView.getBoundsInParent().getMinX();
//        double boardViewY = boardView.getBoundsInParent().getMinY();
//        double ivX = imageView.getBoundsInParent().getMinX();
//        double ivY = imageView.getBoundsInParent().getMinY();
//        Point2D.Double from = boardView.getCellPoisition(Integer.valueOf(origin.getId()));
//        Point2D.Double to = boardView.getCellPoisition(Integer.valueOf(dest.getId()));
        TranslateTransition transition = new TranslateTransition(Duration.millis(1000), imageView);

        transition.setFromX(0);
        transition.setFromY(0);
        transition.setToX(240);
        transition.setToY(0);
        transition.play();
//        TranslateTransition transition = TranslateTransitionBuilder.create()
//                .node(imageView)
//                .fromX(from.x)
//                // .fromX(origin.getBoundsInParent().getMinX())
//                .toX(to.x)
//                .fromY(from.y)
//                .toY(to.y)
//                .duration(Duration.millis(1000))
//                .interpolator(Interpolator.LINEAR)
//                .cycleCount(1)
//                .build();

        return transition;
    }

}
