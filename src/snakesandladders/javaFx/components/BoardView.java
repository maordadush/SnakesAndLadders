/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.components;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author maor
 */
public class BoardView extends GridPane {

    double widthSquere = 100.0;
    double hightSquere = 100.0;

    public BoardView() {

    }

    public BoardView(int o_BoardSize) {
        setGridLinesVisible(true);
         for (int Y = o_BoardSize; Y >= 1; Y--) {
            for (int X = 1; X <= o_BoardSize; X++) {
                add(new Rectangle(widthSquere, hightSquere, new Color(0.1, 0.4, 0.9, 0.5)), X, Y);
            }
        }
        fillButtonsAndNumbers(o_BoardSize);
    }

    private void fillButtonsAndNumbers(int o_BoardSize) {

        for (int Y = o_BoardSize; Y >= 1; Y--) {
            for (int X = 1; X <= o_BoardSize; X++) {
                SquereView bs = new SquereView(String.valueOf(X + (o_BoardSize - Y) * o_BoardSize));
                bs.setMaxWidth(Double.MAX_VALUE);
                bs.setMaxHeight(Double.MAX_VALUE);
                this.add(bs, X, Y);

//                eChars bsType = bs.getType();
//                boardString.append(String.format("%02d", bs.getSquareNumber())).append("|");
//                if (bsType == eChars.LADDER_TAIL || bsType == eChars.SNAKE_HEAD) {
//                    boardString.append(String.format("%02d", bs.getJumpTo().getSquareNumber())).append("|");
//                } else {
//                    boardString.append("--|");
//                }
//                for (int k = 0; k < numPlayers; k++) {
//                    boardString.append(players.get(k).getNumSoldiersAtSquare(bs));
//                }
//                boardString.append(" ");
            }

//            boardString.append(System.lineSeparator());
        }
//        boardString.append("-----------------------------------------------------------------------").append(System.lineSeparator());
//        for (SinglePlayer player : players) {
//            boardString.append("Player " + player.getPlayerID() + ": " + player.getPlayerName() + "\t");
//        }
//        boardString.append(System.lineSeparator());
//
//        //Print out the board
//        System.out.print(boardString.toString());
    }

}
