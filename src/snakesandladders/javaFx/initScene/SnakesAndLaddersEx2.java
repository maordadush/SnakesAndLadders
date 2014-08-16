/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.javaFx.initScene;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import snakesandladders.javaFx.initScene.SceneInitController;

/**
 *
 * @author Noam
 */
public class SnakesAndLaddersEx2 extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Model model = new Model();

        primaryStage.setTitle("Init Board");

        //if you just want to load the FXML
//        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        //if you want to load the FXML and get access to its controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("SceneInit.fxml");
        fxmlLoader.setLocation(url);
        Parent root = (Parent) fxmlLoader.load(url.openStream());
        SceneInitController welcomeCongetResourcetroller = (SceneInitController) fxmlLoader.getController();
        welcomeCongetResourcetroller.setModel(model);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        };
        
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
