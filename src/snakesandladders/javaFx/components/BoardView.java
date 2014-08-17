/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.javaFx.components;


import java.util.concurrent.locks.ReentrantReadWriteLock;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author maor
 */
public class BoardView  extends GridPane {

    public BoardView() {
        
    }

    public BoardView(int o_BoardSize) {
        setGridLinesVisible(true);
        for (int i = 0; i < o_BoardSize; i++) {
            for (int j = 0; j < o_BoardSize; j++) {
                add(new Rectangle(100.0,100.0,new Color(0.1, 0.4, 0.9, 0.5)),i,j);
            }
            
        }
    }
    
}
