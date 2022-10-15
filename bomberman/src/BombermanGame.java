
import Game.GameScreen;
import Game.Map;

import javafx.application.Application;
import javafx.stage.Stage;

public class BombermanGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Map map = new Map(0);
        GameScreen game = new GameScreen(map);
        game.start();
        primaryStage.setScene(game.getScreen());
        primaryStage.setResizable(false);

        primaryStage.show();
    }
}

