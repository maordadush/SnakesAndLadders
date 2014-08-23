/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.components;

import com.sun.jmx.snmp.BerDecoder;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import snakesandladders.gamemodel.BoardSquare;

public class SquareView extends VBox {

    private BoardSquare boardSquare;
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
        this.boardSquare = boardSquare;
        setId(String.valueOf(boardSquare.getSquareNumber()));
        addSquareNumberLabel();
        addSquareTypeLabel();
        initPlayersImages();
        initPlayersLabels();
        initPlayersHbox();
        createPlayersHBoxs();
        createGeneralHBox();
  
        

        //addSoldiersLabel();
    }

    private void addSquareNumberLabel() {
        Label label = new Label(this.getId());

        getChildren().add(label);
    }

    private void addSquareTypeLabel() {
        switch (boardSquare.getType()) {
            case SNAKE_HEAD:
                getChildren().add(new Label("SnakeHead"));
                break;
            case SNAKE_TAIL:
                getChildren().add(new Label("SnakeTail"));
                break;
            case LADDER_HEAD:
                getChildren().add(new Label("LadderHead"));
                break;
            case LADDER_TAIL:
                getChildren().add(new Label("LadderTail"));
                break;
        }

    }

    public void removeSoldier(int playerNumber, Image soldierImage, int numOfSoldiers) {
        HBox player = m_HboxPlayers.get(playerNumber);
        ImageView imageView = m_ImagePlayers.get(playerNumber);
        Label soldiersCount = m_LabelPlayers.get(playerNumber);

        if (numOfSoldiers == 0) {
            vPlayers.getChildren().remove(player);
        } else {
            imageView.setImage(soldierImage);
            imageView.setFitWidth(30);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            soldiersCount.textProperty().set(String.valueOf(numOfSoldiers));
            soldiersCount.setGraphic(imageView);
            soldiersCount.setContentDisplay(ContentDisplay.TOP);
            soldiersCount.setMaxHeight(150.0);
            player.getChildren().set(0, soldiersCount);
        }
    }

    public void addSoldier(int playerNumber, Image soldierImage, int numOfSoldiers) {
        HBox player = m_HboxPlayers.get(playerNumber);
        ImageView imageView = m_ImagePlayers.get(playerNumber);
        Label soldiersCount = m_LabelPlayers.get(playerNumber);

        if (numOfSoldiers == 0) {
            player.setVisible(true);
        }
        //soldiersCount.textProperty().set((String.valueOf(Integer.valueOf(soldiersCount.getText()) + 1)));
        imageView.setImage(soldierImage);
        imageView.setFitWidth(30);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        soldiersCount.textProperty().set(String.valueOf(numOfSoldiers));
        soldiersCount.setGraphic(imageView);
        soldiersCount.setContentDisplay(ContentDisplay.TOP);
        // player.getChildren().set(0, imageView);
        player.getChildren().set(0, soldiersCount);
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

    public void addSnake() {

  
    }

    public void addLadder(Image ladderImage) {

    }
}
