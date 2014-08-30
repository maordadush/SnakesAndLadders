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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import snakesandladders.javaFx.components.BoardView;

public class MoveTransition extends AnchorPane {
   
    private final TranslateTransition transition;
    private ImageView image;
    private static final double DURATION = 1.0;

    public MoveTransition(double width, double height, double cellSize) {
        maxWidth(width);
        maxHeight(height);
        image = new ImageView();
        image.setVisible(true);
        getChildren().add(image);
        transition = createTransition();
    }

    public void moveSoldier(int fromCell, int toCell, int player) {
        Point2D.Double from = BoardView.getCellPoisition(fromCell);
        Point2D.Double to = BoardView.getCellPoisition(toCell);
       // image.setImage(ImageUtils.getImage());
        image.setVisible(true);
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
