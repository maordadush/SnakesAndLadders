/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.components;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import snakesandladders.gamemodel.BoardSquare;

/**
 *
 * @author maor
 */
public class BoardView extends GridPane {

    double widthSquare = 150.0;
    double heightSquare = 110.0;

    public BoardView() {
    }

    public BoardView(BoardSquare[][] board) {
        setGridLinesVisible(true);
        for (int X = 0; X < board.length; X++) {
            getColumnConstraints().add(new ColumnConstraints(widthSquare));
            getRowConstraints().add(new RowConstraints(heightSquare));
            for (int Y = 0; Y < board.length; Y++) {
                SquareView bs = new SquareView(board[X][Y]);
                bs.setMaxHeight(heightSquare);
                bs.setMaxWidth(widthSquare);
                add(bs, Y, board.length - 1 - X);
            }
        }
    }

}
