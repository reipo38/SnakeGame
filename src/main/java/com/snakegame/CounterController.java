package com.snakegame;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class CounterController {
    private final Image[] nums =
    {
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/zero.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/one.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/two.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/three.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/four.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/five.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/six.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/seven.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/eight.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/numbers/nine.png")))
    };
    @FXML
    private ImageView firstDigit;
    @FXML
    private ImageView secondDigit;
    @FXML
    public void initialize(){
        firstDigit.setImage(nums[0]);
        secondDigit.setImage(nums[0]);
    }

    private final int[] score = {0, 0};

    public void updateScore(){
        if (score[1] != 9){
            score[1]++;
        }
        else if (score[0] != 9){
            score[1] = 0;
            score[0]++;
        }
        else{
            System.out.println("Nigga u win");
        }
        firstDigit.setImage(nums[score[0]]);
        secondDigit.setImage(nums[score[1]]);
    }
}
