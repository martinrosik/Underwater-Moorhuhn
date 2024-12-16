package sk.ukf.moorhuhn;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Ryba extends ImageView {
    private Image[] sprites;
    private Image killed;

    private MediaPlayer vystrelMediaPlayer;
    private MediaPlayer noAmmoMediaPlayer;
    double x, y, maxWidth, maxHeight, rychlost;

    int actImage = 0;
    int stav = 0;

    private Game game;
    private Timeline casovac;

    public Ryba(String sprite, int pocetSprite, double w, double h, double maxW, double maxH, Game game) {
        super();
        this.game = game;
        maxWidth = maxW; maxHeight = maxH;
        sprites = new Image[pocetSprite];
        for (int i = 0; i < pocetSprite; i++) {
            sprites[i] = new Image("/" + sprite + i + ".png", w, h, false, false);
        }
        killed = new Image("/dead.png", w, h, false, false);
        setImage(sprites[0]);

        do {
            rychlost = (int)(-5 + Math.random() * 11) * 30;
        } while (rychlost == 0);
        if (rychlost<0) {
            setLayoutX(maxWidth);
            setScaleX(1);
        }
        else {
            setLayoutX(1);
            setScaleX(-1);
        }
        setLayoutY(150 + (int) (Math.random() * (maxHeight - 200)));
        setOnMousePressed(this::h);

        String path = "src/main/resources/sounds/vystrel.mp3";
        Media vystrelMedia = new Media(new File(path).toURI().toString());
        vystrelMediaPlayer = new MediaPlayer(vystrelMedia);
        vystrelMediaPlayer.setVolume(0.10);

        String path2 = "src/main/resources/sounds/noammo.mp3";
        Media noAmmoMedia = new Media(new File(path2).toURI().toString());
        noAmmoMediaPlayer = new MediaPlayer(noAmmoMedia);
        noAmmoMediaPlayer.setVolume(0.40);
    }

    public void Zmena(double deltaTime) {
        setLayoutX(getLayoutX() + rychlost * deltaTime);
        setLayoutY(getLayoutY() -5 + (int) (Math.random()* 11));
        if ((getLayoutX() < 0) || (getLayoutX() > maxWidth)) stav = 2;
        vykresli();
    }

    private void nextImage() {
        actImage = (actImage + 1) % sprites.length;
    }

    private void vykresli() {
        nextImage();
        if(stav == 0) setImage(sprites[actImage]);
        if(stav == 1) setImage(killed);
        if(stav == 2) setImage(null);
    }

    private void onClick(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            if (game.getAmmo() > 0 && stav != 1) {
                vystrelMediaPlayer.play();
                game.shootAmmo();
                stav = 1;
                game.addScore(20);

                casovac = new Timeline(
                        new KeyFrame(Duration.seconds(1), e -> stav = 2)
                );
                casovac.setCycleCount(1);
                casovac.play();
            } else if (game.getAmmo() < 0) {
                noAmmoMediaPlayer.play();
                System.out.println("NO AMMO! Press R to reload.");
            }
        }
    }

    public double getStav() {
        return stav;
    }

    private void h(MouseEvent evt) {
        onClick(evt);
    }
}
