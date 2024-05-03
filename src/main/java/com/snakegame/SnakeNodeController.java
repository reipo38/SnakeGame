package com.snakegame;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class SnakeNodeController {

    @FXML
    private ImageView imageView;

    @FXML
    void initialize() {
    }

    private final Image vertical = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-body-vertical.png")));
    private final Image horizontal = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-body-horizontal.png")));

    private final Image leftUp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-body-left-up.png")));
    private final Image rightUp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-body-right-up.png")));
    private final Image leftDown = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-body-left-down.png")));
    private final Image rightDown = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-body-right-down.png")));

    private boolean isVertical;

    private turning turn = turning.NONE;

    public void setVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public boolean getVertical() {
        return isVertical;
    }

    public enum turning {
        NONE,
        LEFTUP,
        RIGHTUP,
        LEFTDOWN,
        RIGHTDOWN
    }

    public void setImageView() {
        switch (turn) {
            case NONE -> {
                if (isVertical) {
                    imageView.setImage(vertical);
                } else {
                    imageView.setImage(horizontal);
                }
            }
            case LEFTUP -> imageView.setImage(leftUp);
            case RIGHTUP -> imageView.setImage(rightUp);
            case LEFTDOWN -> imageView.setImage(leftDown);
            case RIGHTDOWN -> imageView.setImage(rightDown);
        }
    }

    public void setTurn(turning turn) {
        this.turn = turn;
    }
}
