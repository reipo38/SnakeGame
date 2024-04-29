package com.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    
    private void startMainloop() throws IOException {
        MainScene mainScene = new MainScene();
        Scene scene = new Scene(mainScene.getRoot(), 600, 680);
        SnakeHeadController headController = mainScene.getHeadController();

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

                    headController.unlockMovement();
                    scene.setOnKeyPressed(headController::handleKeyPressed);

                    try {
                        if (!turnProcessor.processTurn()) {
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

    private void resetApplication() throws IOException {
        startMainloop();
        isTimelineStopped = false;
    }

    public static void main(String[] args) {
        launch();
    }
}