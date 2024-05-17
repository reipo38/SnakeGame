package com.snakegame;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class TurnProcessor {
    private final SnakeNodeManager snakeNodeManager = new SnakeNodeManager();
    private final MainScene mainScene;

    private final ImageView apple;
    private final Parent head;
    private final CounterController counterController;
    private final SnakeHeadController headController;

    public TurnProcessor(MainScene mainScene) {
        this.mainScene = mainScene;
        this.headController = this.mainScene.getHeadController();
        snakeNodeManager.setRoot(mainScene.getRoot());
        apple = this.mainScene.getApple();
        head = this.mainScene.getHead();
        counterController = this.mainScene.getCounterController();
    }

    private boolean isAppleEaten() {
        return (head.getLayoutX() == apple.getLayoutX() && head.getLayoutY() == apple.getLayoutY());
    }

    private boolean isCollidingWithBorder() {
        return head.getLayoutX() < 0 || head.getLayoutX() >= 600 || head.getLayoutY() < 80 || head.getLayoutY() >= 680;
    }

    private void changeAppleLocation() {
        Random rand = new Random();
        int x = rand.nextInt(9) * 60;
        int y = rand.nextInt(1, 10) * 60 + 20;

        while (snakeNodeManager.getPositions().contains(Arrays.toString(new int[]{x, y}))) {
            x = rand.nextInt(9) * 60;
            y = rand.nextInt(1, 10) * 60 + 20;
        }

        apple.setLayoutX(x);
        apple.setLayoutY(y);
    }

    //processTurn returns false to indicate ending the game and stopping the mainloop
    //it could return false if head is colliding with the border, the check for that is performed inside this class
    //it could also return false if snakeNodeManager.updateNodes() returns false, indicating the head has collided with the body

    public boolean processTurn() throws IOException {

        head.setLayoutX(Math.round((head.getLayoutX() + 60 * headController.getX()) / 10.0f) * 10);
        head.setLayoutY(Math.round((head.getLayoutY() + 60 * headController.getY()) / 10.0f) * 10);

        snakeNodeManager.setCurrHeadDirection(headController.getDir());

        if (!snakeNodeManager.updateNodes(new int[]{(int) head.getLayoutX(), (int) head.getLayoutY()}) || isCollidingWithBorder()) {
            head.setLayoutX(head.getLayoutX() - 60 * headController.getX());
            head.setLayoutY(head.getLayoutY() - 60 * headController.getY());
            mainScene.gameOver();
            return false;
        }

        if (isAppleEaten()) {
            changeAppleLocation();
            counterController.updateScore();
            snakeNodeManager.spawnNode();
        }
        return true;
    }

}
