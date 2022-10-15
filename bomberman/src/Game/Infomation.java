package Game;

import Graphics.Sprite;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Infomation {
    private HBox hbox = new HBox();
    private Text time = new Text();
    private Text score = new Text();
    private Text level = new Text();
    private String timeString;
    private String scoreString;
    private String levelString;

    public  Infomation() {
        hbox.getChildren().addAll(time, score, level);
        setBoxlayout();
    }

     public void update(int time, int score, int level) {
        timeString = "Time: " + time;
        scoreString = "Score: " + score;
        levelString = "Level: " + level;

        this.time.setText(timeString);
        this.score.setText(scoreString);
        this.level.setText(levelString);
     }

    public HBox getHbox() {
        return hbox;
    }

    private void setBoxlayout() {
        hbox.setLayoutX(0);
        hbox.setLayoutY(0);
        hbox.setStyle("-fx-background-color: black;");
        HBox.setMargin(time, new Insets(0, 20, 0, 20));
        HBox.setMargin(score, new Insets(0, 20, 0, 20));
        HBox.setMargin(level, new Insets(0, 20, 0, 20));
        hbox.setMinSize(20 * Sprite.SCALED_SIZE, 2 * Sprite.SCALED_SIZE);
        time.setFont(Font.font ("Comic Sans MS", 20));
        score.setFont(Font.font ("Comic Sans MS", 20));
        level.setFont(Font.font ("Comic Sans MS", 20));
        time.setFill(new Color(1, 1, 1, 1));
        score.setFill(new Color(1, 1, 1, 1));
        level.setFill(new Color(1, 1, 1, 1));
    }
}
