package com.snakegame;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class TurnProcessor {
    private final SnakeManager snakeManager = new SnakeManager();
    private final MainScene mainScene;

    private final ImageView apple;
    private final Parent head;
    private final CounterController counterController;

    public TurnProcessor(MainScene mainScene) {
        this.mainScene = mainScene;
        snakeManager.setRoot(mainScene.getRoot());
        snakeManager.setHeadController(mainScene.getHeadController());
        apple = this.mainScene.getApple();
        head = this.mainScene.getHead();
        counterController = this.mainScene.getCounterController();
    }
    private boolean isAppleEaten() {
        return (head.getLayoutX() == apple.getLayoutX() && head.getLayoutY() == apple.getLayoutY());
    }

    private boolean containsPos(double[] pos) {
        return snakeManager.getPositions().stream()
                .anyMatch(arr -> Arrays.equals(arr, pos));
    }

    private void changeAppleLocation() {
        Random rand = new Random();
        double x = rand.nextInt(9) * 60;
        double y = rand.nextInt(1, 10) * 60 + 20;

        while (containsPos(new double[]{x, y})) {
            x = rand.nextInt(9) * 60;
            y = rand.nextInt(1, 10) * 60 + 20;
        }
        apple.setLayoutX(x);
        apple.setLayoutY(y);
    }
    public boolean processTurn(Parent head) throws IOException {

        if (!snakeManager.updateSnake(head)){
            mainScene.gameOver();
            return false;
        }

        if (isAppleEaten()) {
            changeAppleLocation();
            counterController.updateScore();
            snakeManager.spawnSnakeNode();
        }
        return true;
    }

}
