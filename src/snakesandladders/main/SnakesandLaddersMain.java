/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.main;

import snakesandladders.gamecontrol.GameControl;

/**
 *
 * @author Noam
 */
public class SnakesandLaddersMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            GameControl gameControl = new GameControl();
            gameControl.Run();
        } catch (Exception ex) {
            System.out.println("Error occured during the game, exit now...");
        }
    }

}
