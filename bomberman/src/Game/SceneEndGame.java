package Game;

import Graphics.Sprite;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SceneEndGame {

    public static Group screenWin() {
        Group root =  new Group();
        Image image = new Image("file:res/textures/missionpassed.jpg");
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
        return root;
    }

    public static Group screenLose() {
        Group root =  new Group();
        Image image = new Image("file:res/textures/missionfailed.jpg");
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
        return root;
    }

    public static HBox screenResult(int score) {
        HBox pane = new HBox();
        pane.setMinSize(20 * Sprite.SCALED_SIZE, 15 * Sprite.SCALED_SIZE);
        pane.setStyle("-fx-background-color: black;");
        Text text = new Text();
        text.setText("Score: " + score);
        text.setFont(Font.font ("Titan One", 50));
        HBox.setMargin(text, new Insets(200));
        pane.getChildren().add(text);
        text.setFill(new Color(1,1,1, 1));
        return pane;
    }
}


