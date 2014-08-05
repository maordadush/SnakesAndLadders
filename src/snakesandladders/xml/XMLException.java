/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.xml;

/**
 *
 * @author Noam
 */
public class XMLException extends Exception {

    public XMLException(String Message) {
        super(Message);
    }

    @Override
    public String toString() {
        return "XMLException";
    }
}
