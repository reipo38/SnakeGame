package com.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

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
        SnakeHeadController headController = mainScene.getHeadController();

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

        TurnProcessor turnProcessor = new TurnProcessor(mainScene);


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
                        if (!turnProcessor.processTurn(head)){
                          stopTimeline();
                          mainScene.getRoot().getChildren().add(resetButton);
                          resetButton.setLayoutX(230);
                          resetButton.setLayoutY(40);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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

    public static void main(String[] args) {
        launch();
    }
}