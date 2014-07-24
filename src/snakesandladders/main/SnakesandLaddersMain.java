/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.main;

import java.util.Scanner;
import snakesandladders.consoleview.ConsoleView;
import snakesandladders.gamecontrol.GameControl;
import snakesandladders.gamemodel.SnakesAndLaddersSingleGame;

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
            //SnakesAndLaddersSingleGame game = new SnakesAndLaddersSingleGame(5,2);
            //ConsoleView m_consoleView = new ConsoleView();
            //m_consoleView.printGame(game);
        } catch (Exception ex) {
            System.out.println("Error found. Bye bye");
        }
    }
    
}
