/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.components;

import java.util.Set;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import snakesandladders.gamemodel.BoardSquare;
import snakesandladders.gamemodel.eChars;

public class SquareView extends VBox {

    private BoardSquare boardSquare;
    private Set<HBox> players;

    SquareView(BoardSquare boardSquare) {
        this.boardSquare = boardSquare;
        setId(String.valueOf(boardSquare.getSquareNumber()));
        addSquareNumberLabel();
        addSquareTypeLabel();
        
    }

    private void addSquareNumberLabel() {
        Label label = new Label(this.getId());
        getChildren().add(label);
    }

    private void addSquareTypeLabel() {
        switch(boardSquare.getType()){
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

}
