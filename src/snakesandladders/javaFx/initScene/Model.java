/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.javaFx.initScene;

/**
 *
 * @author Noam
 */
class Model {
    public String getLoginResult(String username, String password){
        if (username != null && !username.isEmpty()){
            return "success!";
        } else {
            return "username can not be empty!";
        }
    }
}
