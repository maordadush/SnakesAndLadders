/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.awt.Point;

/**
 *
 * @author Noam
 */
public class BoardSquare extends Point {
    eChars type = eChars.NONE;

    public eChars getType() {
        return type;
    }

    public void setType(eChars type) {
        this.type = type;
    }

}
