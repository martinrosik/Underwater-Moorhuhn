package sk.ukf.moorhuhn;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 600);

        StartMenu startMenu = new StartMenu(root);

        root.getChildren().add(startMenu);

        stage.getIcons().add(new Image("/icon.png"));
        stage.setTitle("Underwater Moorhuhn");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

