module com.example.snakegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.snakegame to javafx.fxml;
    exports com.snakegame;
}