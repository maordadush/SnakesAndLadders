/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.javaFx.utils;


import java.awt.geom.Point2D;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.eChars;
import snakesandladders.javaFx.components.BoardView;
import snakesandladders.javaFx.components.SquareView;
import snakesandladders.players.SinglePlayer;
import snl.Players;
import snl.Players.Player;

public class MoveTransition extends AnchorPane {
   
    private final TranslateTransition transition;
    public ImageView image;
    private static final double DURATION = 2.0;
    private final BoardView boardView;


    public MoveTransition(double width, double height, BoardView boardView) {
        maxWidth(width);
        maxHeight(height);
        this.boardView = boardView;
        image = new ImageView();
        image.setFitWidth(30);
        image.setPreserveRatio(true);
        image.setSmooth(true);
        image.setCache(true);
        image.setVisible(true);
        getChildren().add(image);
        transition = createTransition();
    }

    public void moveSoldier(int fromCell, int toCell, final Image soldierImage, final SinglePlayer player, final SquareView dest, final BoardSquare toMove) {
       
        Point2D.Double from = boardView.getCellPoisition(fromCell);
        Point2D.Double to = boardView.getCellPoisition(toCell);
        image.setImage(soldierImage);
        image.setVisible(true);
       transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                image.setVisible(false);
                dest.addSoldier(player.getPlayerID(),soldierImage , player.getNumSoldiersAtSquare(toMove));
                
            }
        });
        moveSoldierStart(from, to);

    }
    
    private TranslateTransition createTransition() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(DURATION), this.image);
        translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                image.setVisible(false);
                
            }
        });
        return translateTransition;
    }

    private void moveSoldierStart(Point2D.Double from, Point2D.Double to) {
        transition.setFromX(from.x);
        transition.setFromY(from.y);
        transition.setToX(to.x);
        transition.setToY(to.y);
        transition.play();
    }
}
