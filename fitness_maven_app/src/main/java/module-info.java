module com.example.fitness_app {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fitness_app.ui to javafx.fxml;
    exports com.example.fitness_app;
}
