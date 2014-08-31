/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.components;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import snakesandladders.gamemodel.BoardSquare;

public class SquareView extends VBox {

    String squereStyleEven = "-fx-background-color: oldlace;"
            + "-fx-border-style: solid;"
            + "-fx-border-width: 1;"
            + "-fx-border-color: black";
    String squereStyleOdd = "-fx-background-color: blanchedalmond;"
            + "-fx-border-style: solid;"
            + "-fx-border-width: 1;"
            + "-fx-border-color: black";

    private final BoardSquare boardSquare;
    private HBox vPlayers;
    private ImageView imagePlayer1;
    private ImageView imagePlayer2;
    private ImageView imagePlayer3;
    private ImageView imagePlayer4;
    private Label labelCountSoldierPlayer1;
    private Label labelCountSoldierPlayer2;
    private Label labelCountSoldierPlayer3;
    private Label labelCountSoldierPlayer4;
    private HBox hplayer1;
    private HBox hplayer2;
    private HBox hplayer3;
    private HBox hplayer4;
    private List<ImageView> m_ImagePlayers;
    private List<Label> m_LabelPlayers;
    private List<HBox> m_HboxPlayers;

    SquareView(BoardSquare boardSquare) {
        int squereNumber = boardSquare.getSquareNumber();
        this.boardSquare = boardSquare;
        setId(String.valueOf(squereNumber));
        addSquareNumberLabel();
        initPlayersImages();
        initPlayersLabels();
        initPlayersHbox();
        createPlayersHBoxs();
        createGeneralHBox();

        if ((squereNumber % 2) == 0) {
            setStyle(squereStyleEven);
        } else {
            setStyle(squereStyleOdd);
        }
    }

    private void addSquareNumberLabel() {
        Label label = new Label(this.getId());

        getChildren().add(label);
    }

    public void removeSoldier(int playerNumber, Image soldierImage, int numOfSoldiers) {
        HBox player = m_HboxPlayers.get(playerNumber - 1);
        ImageView imageView = m_ImagePlayers.get(playerNumber - 1);
        Label soldiersCount = m_LabelPlayers.get(playerNumber - 1);

        if (numOfSoldiers == 0) {
            player.getChildren().remove(soldiersCount);
        } else {
            imageView.setImage(soldierImage);
            imageView.setFitWidth(30);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            soldiersCount.textProperty().set(String.valueOf(numOfSoldiers));
            soldiersCount.setGraphic(imageView);
            soldiersCount.setContentDisplay(ContentDisplay.TOP);
            player.getChildren().setAll(soldiersCount);
        }
    }

    public void addSoldier(int playerNumber, Image soldierImage, int numOfSoldiers) {
        HBox player = m_HboxPlayers.get(playerNumber - 1);
        ImageView imageView = m_ImagePlayers.get(playerNumber - 1);
        Label soldiersCount = m_LabelPlayers.get(playerNumber - 1);

        imageView.setImage(soldierImage);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        soldiersCount.textProperty().set(String.valueOf(numOfSoldiers));
        soldiersCount.setGraphic(imageView);
        soldiersCount.setContentDisplay(ContentDisplay.TOP);
        player.getChildren().setAll(soldiersCount);
        player.setAlignment(Pos.TOP_CENTER);

    }

    private void initPlayersImages() {
        imagePlayer1 = new ImageView();
        imagePlayer2 = new ImageView();
        imagePlayer3 = new ImageView();
        imagePlayer4 = new ImageView();

        m_ImagePlayers = new ArrayList<>();
        m_ImagePlayers.add(imagePlayer1);
        m_ImagePlayers.add(imagePlayer2);
        m_ImagePlayers.add(imagePlayer3);
        m_ImagePlayers.add(imagePlayer4);

    }

    private void initPlayersLabels() {
        labelCountSoldierPlayer1 = new Label();
        labelCountSoldierPlayer2 = new Label();
        labelCountSoldierPlayer3 = new Label();
        labelCountSoldierPlayer4 = new Label();

        m_LabelPlayers = new ArrayList<>();
        m_LabelPlayers.add(labelCountSoldierPlayer1);
        m_LabelPlayers.add(labelCountSoldierPlayer2);
        m_LabelPlayers.add(labelCountSoldierPlayer3);
        m_LabelPlayers.add(labelCountSoldierPlayer4);

    }

    private void initPlayersHbox() {
        hplayer1 = new HBox(10);
        hplayer2 = new HBox(10);
        hplayer3 = new HBox(10);
        hplayer4 = new HBox(10);

        m_HboxPlayers = new ArrayList<>();
        m_HboxPlayers.add(hplayer1);
        m_HboxPlayers.add(hplayer2);
        m_HboxPlayers.add(hplayer3);
        m_HboxPlayers.add(hplayer4);
    }

    private void createPlayersHBoxs() {
        for (int i = 0; i < m_HboxPlayers.size(); i++) {
            m_HboxPlayers.get(i).getChildren().addAll(m_LabelPlayers.get(i));
        }
    }

    private void createGeneralHBox() {
        vPlayers = new HBox(8);

        for (HBox playerBox : m_HboxPlayers) {
            vPlayers.getChildren().add(playerBox);
        }
        vPlayers.setMaxHeight(120.0);
        getChildren().add(vPlayers);
    }

    public ImageView getSoldierImage(int playerID) {
        return m_ImagePlayers.get(playerID - 1);
    }
}
