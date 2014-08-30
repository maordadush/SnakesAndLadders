/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.components;

import java.awt.geom.Point2D;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import snakesandladders.gamemodel.BoardSquare;

/**
 *
 * @author maor
 */
public class BoardView extends GridPane {

    final static double widthSquare = 160.0;
    final static double heightSquare = 110.0;
    private int boardSize;
    

    public BoardView() {
    }

    public BoardView(BoardSquare[][] board) {
         this.boardSize = board.length;
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
    
    public Point2D.Double getCellPoisition(int squareNumber) {
        
        double y = (boardSize - (int) ((squareNumber - 1) / boardSize) - 0.9) * heightSquare;
        double x = squareNumber % boardSize;
        if (x == 0) {
            x = boardSize;
        }
        x = (x - 0.9) * widthSquare;
        
        Point2D.Double p = new Point2D.Double(x, y);
        return p;
    }


}
