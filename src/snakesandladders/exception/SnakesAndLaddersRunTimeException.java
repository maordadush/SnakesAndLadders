/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.exception;

/**
 *
 * @author Noam
 */
public class SnakesAndLaddersRunTimeException extends Exception {

    public SnakesAndLaddersRunTimeException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "SnakesAndLaddersRunTimeException";
    }
    
    
}
