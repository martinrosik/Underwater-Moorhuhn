package sk.ukf.moorhuhn;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

public class Game extends Group {
    final int SPRITESIZEX = 70;
    final int SPRITESIZEY = 45;
    final int MAXFISH = 15;

    private int ammoCount = 10;
    private final int MAX_AMMO = 10;

    private ImageView[] ammoSprites;
    private Image ammoImage;
    private ImageView background;

    private LinkedList<Ryba> zoznam;

    private double maxWidth, maxHeight;

    private Text timerText;
    private int timeLeft = 30;

    private Text scoreText;
    private int score = 0;

    private Text gameOverText;
    private boolean gameOver = false;

    private MediaPlayer backgroundMusicPlayer;
    private MediaPlayer alertMusicPlayer;

    public Game(int w, int h, String pozadie, int enem) {
        maxWidth = w; maxHeight = h;
        Image bg = new Image("/" + pozadie, w, h, false, false);
        ammoImage = new Image("/ammo.png", 30, 30, false, false);
        background = new ImageView(bg);

        getChildren().add(background);
        zoznam = new LinkedList<>();

        ammoSprites = new ImageView[MAX_AMMO];
        for (int i = 0; i < ammoCount; i++) {
            ammoSprites[i] = new ImageView(ammoImage);
            ammoSprites[i].setLayoutX(10 + (i * 30));
            ammoSprites[i].setLayoutY(30);
            getChildren().add(ammoSprites[i]);
        }

        timerText = new Text();
        timerText.setFont(Font.font("Impact", 36));
        timerText.setFill(Color.WHITE);
        timerText.setStroke(Color.BLACK);
        timerText.setLayoutX(maxWidth - 100);
        timerText.setLayoutY(50);
        updateTimerText();
        getChildren().add(timerText);

        scoreText = new Text();
        scoreText.setFont(Font.font("Impact", 44));
        scoreText.setFill(Color.YELLOW);
        scoreText.setStroke(Color.BLACK);
        scoreText.setLayoutX(maxWidth / 2 - 100);
        scoreText.setLayoutY(50);
        updateScoreText();
        getChildren().add(scoreText);

        String path = "src/main/resources/sounds/hudba.mp3";
        Media backgroundMedia = new Media(new File(path).toURI().toString());
        backgroundMusicPlayer = new MediaPlayer(backgroundMedia);
        backgroundMusicPlayer.setVolume(0.2);
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicPlayer.setAutoPlay(true);

        String path2 = "src/main/resources/sounds/alert.mp3";
        Media alertMedia = new Media(new File(path2).toURI().toString());
        alertMusicPlayer = new MediaPlayer(alertMedia);
        alertMusicPlayer.setVolume(0.5);
        alertMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R && !gameOver) {
                resetAmmo();
            }
        });
        setFocusTraversable(true);
    }

    public void update(double deltaTime) {
        if (timeLeft <= 0) {
            gameOver();
            return;
        }

        if(timeLeft < 11) {
            backgroundMusicPlayer.stop();
            alertMusicPlayer.play();
        }

        VytvorRybu();
        UrobPohyb(deltaTime/1000000000);
        PremazRyby();
    }

    public void decrementTimer() {
        if (timeLeft > 0) {
            timeLeft--;
            updateTimerText();
        }
    }

    private void updateTimerText() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateScoreText() {
        scoreText.setText("Score: " + score);
    }

    public void addScore(int points) {
        score += points;
        updateScoreText();
    }

    public int getAmmo() {
        return ammoCount;
    }

    public void shootAmmo() {
        ammoCount--;
        Ammo();
    }

    public void resetAmmo() {
        if (getAmmo() < 3) {
            ammoCount = MAX_AMMO;
            Ammo();
        }
    }

    private void Ammo() {
        getChildren().removeAll(ammoSprites);

        ammoSprites = new ImageView[MAX_AMMO];
        for (int i = 0; i < ammoCount; i++) {
            ammoSprites[i] = new ImageView(ammoImage);
            ammoSprites[i].setLayoutX(10 + (i * 30));
            ammoSprites[i].setLayoutY(30);
            getChildren().add(ammoSprites[i]);
        }
    }

    private void VytvorRybu() {
        if (zoznam.size() < MAXFISH) {
            if(Math.random() < 0.3) {
                Ryba ryba = new Ryba("ryba", 4, SPRITESIZEX, SPRITESIZEY, maxWidth, maxHeight, this);
                zoznam.add(ryba);
                getChildren().add(ryba);
            }
        }
    }

    private void UrobPohyb(double delta) {
        Iterator<Ryba> iterator = zoznam.iterator();
        while (iterator.hasNext()) {
            Object objekt = iterator.next();
            if (objekt instanceof Ryba) {
                ((Ryba) objekt).Zmena(delta);
            }
        }
    }

    private void PremazRyby() {
        Iterator<Ryba> iterator = zoznam.iterator();
        while (iterator.hasNext()) {
            Ryba ryba = iterator.next();
            if (ryba.getStav() == 2) {
                iterator.remove();
                getChildren().remove(ryba);
            }
        }
    }

    private void PremazZiveRyby() {
        Iterator<Ryba> iterator = zoznam.iterator();
        while (iterator.hasNext()) {
            Ryba ryba = iterator.next();
            if (ryba.getStav() == 0) {
                iterator.remove();
                getChildren().remove(ryba);
            }
        }
    }

    private void gameOver() {
        if(timeLeft == 0) {
            gameOver = true;
            gameOverText = new Text("GAME OVER");
            gameOverText.setFont(Font.font("Impact", 64));
            gameOverText.setFill(Color.RED);
            gameOverText.setStroke(Color.BLACK);
            gameOverText.setLayoutX(maxWidth / 2 - 140);
            gameOverText.setLayoutY(maxHeight / 2);
            getChildren().add(gameOverText);
            backgroundMusicPlayer.stop();
            alertMusicPlayer.stop();
            PremazZiveRyby();
            PremazRyby();
            getChildren().removeAll(ammoSprites);
            getChildren().removeAll(timerText);
        }
    }
}
