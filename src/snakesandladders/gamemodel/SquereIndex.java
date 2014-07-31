/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

/**
 *
 * @author Noam
 */
public class SquereIndex {

    public class SquareIndex {

        private int x;
        private int y;

        public SquareIndex() {
            x = 0;
            y = 0;
        }

        public SquareIndex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setSquare(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
