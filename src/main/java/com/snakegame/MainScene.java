package com.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class MainScene {
    private final ImageView apple;
    private final Parent head;
    private final Pane root;
    private final SnakeHeadController headController;
    private final CounterController counterController;

    public MainScene() throws IOException {
        ImageView topPanel = new ImageView();
        topPanel.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/top-panel.png")).toExternalForm()));

        ImageView board = new ImageView();
        board.setImage(new Image(Objects.requireNonNull(getClass().getResource("/images/board.png")).toExternalForm()));

        ImageView appleIcon = new ImageView();
        appleIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/apple.png"))));

        //snake head setup
        FXMLLoader headLoader = new FXMLLoader(getClass().getResource("/com/snakegame/SnakeHead.fxml"));
        head = headLoader.load();
        headController = headLoader.getController();

        //counter setup
        FXMLLoader countLoader = new FXMLLoader(getClass().getResource("/com/snakegame/Counter.fxml"));
        Parent counter = countLoader.load();
        counterController = countLoader.getController();

        apple = new ImageView();
        apple.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/apple.png"))));

        root = new Pane();
        root.getChildren().addAll(topPanel, appleIcon, board, head, apple, counter);

        topPanel.toFront();
        topPanel.setLayoutX(0);
        topPanel.setLayoutY(0);

        head.toFront();
        head.setLayoutX(0);
        head.setLayoutY(620);

        appleIcon.toFront();
        appleIcon.setX(30);
        appleIcon.setY(10);

        counter.toFront();
        counter.setLayoutX(100);
        counter.setLayoutY(10);

        board.setLayoutX(0);
        board.setLayoutY(80);

        apple.toFront();
        Random rand = new Random();
        int x = rand.nextInt(9) * 60;
        int y = rand.nextInt(1, 10) * 60 + 20;
        apple.setLayoutX(x);
        apple.setLayoutY(y);

    }

    public Pane getRoot() {
        return root;
    }

    public Parent getHead() {
        return head;
    }

    public SnakeHeadController getHeadController() {
        return headController;
    }

    public ImageView getApple() {
        return apple;
    }

    public CounterController getCounterController() {
        return counterController;
    }

    public void gameOver() {
        ImageView gameOver = new ImageView();
        gameOver.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/game-over.png"))));
        root.getChildren().add(gameOver);
        gameOver.setLayoutX(100);
        gameOver.setY(200);


    }
}
