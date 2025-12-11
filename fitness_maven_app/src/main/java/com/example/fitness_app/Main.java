package com.example.fitness_app;

import com.example.fitness_app.ui.FitnessView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        FitnessView view = new FitnessView();
        Scene scene = new Scene(view.getRoot(), 900, 600);
        stage.setTitle("Fitness App");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() {
        System.out.println("Приложение закрыто");
    }


    public static void main(String[] args) {
        launch();
    }
}
