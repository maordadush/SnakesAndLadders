/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.gamecontrol;

import snakesandladders.consoleview.ConsoleView;
import snakesandladders.gamemodel.GameModel;

/**
 *
 * @author Noam
 */
public class GameControl {

    private ConsoleView m_consoleView;
    private GameModel m_gameModel;
    
    public GameControl() {
        m_gameModel = new GameModel();
        m_consoleView = new ConsoleView();
    }
    
    public void Run()
    {
        
    }
    
    
}
