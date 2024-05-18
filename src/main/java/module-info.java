module com.snakegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.snakegame to javafx.fxml;
    exports com.snakegame;
}