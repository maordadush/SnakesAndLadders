/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.players;

import snakesandladders.xml.XMLException;
import snl.PlayerType;

/**
 *
 * @author Noam
 */
public enum ePlayerType {

    Human, Computer;
    
    public static ePlayerType GetTypeFromXML(PlayerType type) throws XMLException {
        switch (type) {
            case HUMAN:
                return Human;
            case COMPUTER:
                return Computer;
            default:
                throw new XMLException("GetTypeFromXML(): Invalid PlayerType");
        }
    }
}
