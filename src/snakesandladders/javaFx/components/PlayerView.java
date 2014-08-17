package snakesandladders.javaFx.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.VBox;
import snakesandladders.javaFx.utils.ImageUtils;
import snakesandladders.players.ePlayerType;


public class PlayerView extends VBox{

    public PlayerView(String title, ePlayerType type) {
        setSpacing(10);
        setAlignment(Pos.CENTER);
        getChildren().addAll(createImage(type), createLabel(title));
    }
    
    private Label createLabel(String title){
        return new Label(title);
    }
    
    private ImageView createImage(ePlayerType type){
        return ImageViewBuilder
                .create()
                .image(getImage(type))
                .build();
    }

    private Image getImage(ePlayerType type) {
        String filename = type == ePlayerType.HUMAN ? "human" : "computer";
        return ImageUtils.getImage(filename);
    }
}