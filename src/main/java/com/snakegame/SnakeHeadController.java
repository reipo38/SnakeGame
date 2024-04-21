package com.snakegame;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

public class SnakeHeadController {

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        imageView.setImage(Up);
    }
    public enum direction {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    private int x = 0;
    private int y = -1;
    private direction dir = direction.UP;
    private final Image Left = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-head-left.png")));
    private final Image Up = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-head-up.png")));
    private final Image Right = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-head-right.png")));
    private final Image Down = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-head-down.png")));
    private boolean isTurnPlayed = false;

    void handleKeyPressed(KeyEvent event) {
        if (Application.isTimelineStopped() || isTurnPlayed) return;

        switch (event.getCode()) {
            case LEFT:
                dir = direction.values()[(dir.ordinal() - 1 + 4) % 4];
                break;
            case RIGHT:
                dir = direction.values()[(dir.ordinal() + 1) % 4];
                break;
            default:
                break;
        }
        updateMovement();
        isTurnPlayed = true;
    }

    private void updateMovement() {
        switch (dir) {
            case LEFT:
                imageView.setImage(Left);
                x = -1;
                y = 0;
                break;
            case UP:
                imageView.setImage(Up);
                x = 0;
                y = -1;
                break;
            case RIGHT:
                imageView.setImage(Right);
                x = 1;
                y = 0;
                break;
            case DOWN:
                imageView.setImage(Down);
                x = 0;
                y = 1;
                break;
        }
    }
    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public direction getDir(){
        return dir;
    }
    public void unlockMovement(){isTurnPlayed = false;}

}
