package com.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;

public class Application extends javafx.application.Application {
    private Timeline timeline;
    private Stage stage;
    private static boolean isTimelineStopped = false;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        startMainloop();
    }
    private void startMainloop() throws IOException{

        MainScene mainScene = new MainScene();
        Scene scene = new Scene(mainScene.getRoot(), 600, 680);
        Parent head = mainScene.getHead();
        ImageView apple = mainScene.getApple();
        SnakeHeadController headController = mainScene.getHeadController();
        CounterController counterController = mainScene.getCounterController();

        head.setLayoutX(0);
        head.setLayoutY(620);

        Button resetButton = new Button("Start Again");
        resetButton.setOnAction(event -> {
            try {
                resetApplication();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        SnakeManager mngr = new SnakeManager(mainScene.getRoot(), headController);

        //changeAppleLocation() takes mngr because it needs to access its containsPos() method in order
        //to not place the apple on Nodes.
        changeAppleLocation(apple, mngr);

        stage.setTitle("The Snake Game");
        stage.setScene(scene);
        stage.show();
        timeline = new Timeline(

                new KeyFrame(Duration.seconds(0.70), event -> {
                    /*
                    getLayoutX and Y return double, not int
                    because operations with floating point numbers aren't always as precise as needed
                    there is logic for rounding to the closest number, divisible by ten
                    */

                    head.setLayoutX(Math.round((head.getLayoutX() + 60 * headController.getX()) / 10.0f)*10);
                    head.setLayoutY(Math.round((head.getLayoutY() + 60 * headController.getY()) / 10.0f)*10);

                    headController.unlockMovement();
                    scene.setOnKeyPressed(headController::handleKeyPressed);

                    try {
                        mngr.updateSnake(head);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (isCollidingHeadAndApple(head, apple)) {
                        changeAppleLocation(apple, mngr);
                        counterController.updateScore();
                        mngr.spawnSnakeNode();
                    }

                    if (head.getLayoutX() < 0 || head.getLayoutX() >= 600 || head.getLayoutY() < 80 || head.getLayoutY() >= 680 || mngr.isCollidingHeadAndNode(head)) {
                        stopTimeline();
                        head.setLayoutX(head.getLayoutX() - 60 * headController.getX());
                        head.setLayoutY(head.getLayoutY() - 60 * headController.getY());
                        mainScene.gameOver();
                        mainScene.getRoot().getChildren().add(resetButton);
                        resetButton.setLayoutX(230);
                        resetButton.setLayoutY(40);
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static boolean isTimelineStopped() {
        return isTimelineStopped;
    }
    private void stopTimeline() {
        timeline.stop();
        isTimelineStopped = true;
    }
    private void resetApplication() throws IOException{
        startMainloop();
        isTimelineStopped = false;
    }
    private void changeAppleLocation(ImageView apple, SnakeManager mngr) {
        Random rand = new Random();
        double x = rand.nextInt(9) * 60;
        double y = rand.nextInt(1, 10) * 60 + 20;

        while (mngr.containsPos(new double[]{x, y})){
            x = rand.nextInt(9) * 60;
            y = rand.nextInt(1, 10) * 60 + 20;
        }
        apple.setLayoutX(x);
        apple.setLayoutY(y);
    }

    private boolean isCollidingHeadAndApple(Parent head, ImageView apple) {
        return (head.getLayoutX() == apple.getLayoutX() && head.getLayoutY() == apple.getLayoutY());
    }

    public static void main(String[] args) {
        launch();
    }
}