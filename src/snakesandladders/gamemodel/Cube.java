/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

import java.util.Random;

/**
 *
 * @author Noam
 */
public class Cube {

    public int throwCube() {
        Random rand = new Random();

        int randomNum = rand.nextInt(5) + 1;

        return randomNum;
    }

}
