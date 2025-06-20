package com.example;

import javafx.application.Application;
import javafx.stage.Stage;

// point d'entrée du programme
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        HotelView view = new HotelView(primaryStage);
        HotelController ctrl = new HotelController(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
