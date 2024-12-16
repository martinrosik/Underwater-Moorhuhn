package sk.ukf.moorhuhn;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartMenu extends Group {
    private ImageView menuBackground;

    public StartMenu(Group root) {
        Image menuImage = new Image("/menuBackground.png", 1200, 600, false, false);
        menuBackground = new ImageView(menuImage);
        menuBackground.setX(0);
        menuBackground.setY(0);

        Text welcomeText = new Text("UNDERWATER-Moorhuhn");
        welcomeText.setFont(Font.font("Impact", 60));
        welcomeText.setFill(Color.WHITE);
        welcomeText.setLayoutX(310);
        welcomeText.setLayoutY(220);

        Button startButton = new Button("START GAME");
        startButton.setFont(Font.font("Impact", 20));
        startButton.setTextFill(Color.GREEN);
        startButton.setLayoutX(535);
        startButton.setLayoutY(250);
        startButton.setOnAction(event -> {
            startGame(root);
        });

        getChildren().addAll(menuBackground, startButton, welcomeText);
    }

    private void startGame(Group root) {
        root.getChildren().clear();
        Game game = new Game(1200, 600, "background.png", 15);
        game.requestFocus();
        root.getChildren().add(game);
        MyTimer timer = new MyTimer(game);
        timer.start();
    }
}

