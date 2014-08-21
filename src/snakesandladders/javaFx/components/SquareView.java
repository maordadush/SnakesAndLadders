/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.eChars;
import snakesandladders.players.SinglePlayer;
import snakesandladders.players.Soldier;

public class SquareView extends VBox {

    private BoardSquare boardSquare;
    private VBox vPlayers;
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
        createGeneralVBox();

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

    //UI not need to hold Logic
//    private void addSoldiersLabel() {
//        List<SinglePlayer> players = boardSquare.getPlayers();
//        for (SinglePlayer singlePlayer : players) {
//            HBox hbox = new HBox();
//            hbox.getChildren().addAll(new Label("Name: "), new Label(singlePlayer.getPlayerName()));
//            int numOfSoldiers = singlePlayer.getNumSoldiersAtSquare(boardSquare);
//            for (int i = 0; i < numOfSoldiers; i++) {
//                hbox.getChildren().addAll(new Label(" "),new Label(String.valueOf(singlePlayer.getM_SoldiersList().get(i).getSoldierID())));
//            }
//            getChildren().add(hbox);
//        }
//    }
    public void removeSoldier(int playerNumber) {
        //This function knows from outside
    }

    public void addSoldier(int playerNumber) {
        //This fuction knows from outside
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
            m_HboxPlayers.get(i).getChildren().addAll(m_ImagePlayers.get(i), m_LabelPlayers.get(i));
        }
    }

    private void createGeneralVBox() {
        vPlayers = new VBox(8);
        
        for(HBox playerBox : m_HboxPlayers){
            vPlayers.getChildren().add(playerBox);
        }
    }
    
}
