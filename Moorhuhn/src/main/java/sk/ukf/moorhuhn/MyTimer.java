package sk.ukf.moorhuhn;

import javafx.animation.AnimationTimer;

public class MyTimer extends AnimationTimer {
    private Game myGame;
    private long lastNanoTime;
    private long lastTimerUpdate = 0;

    public MyTimer(Game game) {
        myGame = game;
    }

    public void handle(long now) {
        myGame.update(now - lastNanoTime);
        lastNanoTime = now;

        if (now - lastTimerUpdate >= 1000000000) {
            myGame.decrementTimer();
            lastTimerUpdate = now;
        }

        try {
            Thread.sleep(50);
        } catch (Exception e) {}
    }

    public void start() {
        lastNanoTime = System.nanoTime();
        super.start();
    }
}
