package com.example;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * Fenêtre de confirmation pour libérer une chambre
 */
public class ConfirmFreeRoomBoxView {

    private static boolean answer = false;
    private static final int MIN_WIDTH = 300;

    public static boolean display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); 
        window.setTitle("Free Room Confirmation");
        window.setMinWidth(MIN_WIDTH);

        Label label = new Label("Are you sure you want to free this room?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> { // l'utilisateur a confirmé la libération de la chambre en appuyant sur Yes
            answer = true; 
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); 

        return answer;
    }
}
