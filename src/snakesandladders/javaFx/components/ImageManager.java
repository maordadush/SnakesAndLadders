/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snakesandladders.javaFx.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import snakesandladders.javaFx.utils.ImageUtils;

/**
 *
 * @author Noam
 */
public class ImageManager {
    
    public static Label createLabel(String title){
        return new Label(title);
    }
    
    public static ImageView createImage(String FileName){
        return ImageViewBuilder
                .create()
                .image(getImage(FileName))
                .build();
    }

    public static Image getImage(String FileName) {
        String filename = FileName;
        return ImageUtils.getImage(filename);
    }
    
}
