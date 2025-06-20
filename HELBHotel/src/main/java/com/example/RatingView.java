package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RatingView {

    public static int askForRating() {
        final Stage ratingWindow = new Stage();
        ratingWindow.initModality(Modality.APPLICATION_MODAL);
        ratingWindow.setTitle("Rate Your Stay");
        ratingWindow.setMinWidth(350);
        ratingWindow.setResizable(false);

        ratingWindow.setOnCloseRequest(event -> event.consume());

        Label label = new Label("Please rate your stay:");
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox starsBox = new HBox(10);
        starsBox.setAlignment(Pos.CENTER);
        Label[] starLabels = new Label[5];
        final int[] selectedRating = {0};

        for (int i = 0; i < 5; i++) {
            final int index = i;
            Label star = new Label("☆");
            final int ratingValue = index + 1;

            star.setOnMouseEntered(e -> updateStars(starLabels, index, "★", "gold"));
            star.setOnMouseExited(e -> updateStars(starLabels, selectedRating[0] - 1, "★", "gold"));
            star.setOnMouseClicked(e -> {
                selectedRating[0] = ratingValue;
                updateStars(starLabels, index, "★", "gold");
            });

            starLabels[i] = star;
            starsBox.getChildren().add(star);
        }

        Button submit = new Button("Submit");
        submit.setStyle("-fx-font-size: 13px; -fx-padding: 5 15;");
        submit.setOnAction(e -> {
            if (selectedRating[0] > 0) {
                ratingWindow.close();
            } else {
                showWarning(ratingWindow);
            }
        });

        VBox layout = new VBox(20, label, starsBox, submit);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(layout);
        ratingWindow.setScene(scene);
        ratingWindow.showAndWait();

        return selectedRating[0];
    }

    private static void updateStars(Label[] stars, int upTo, String filled, String color) {
        for (int j = 0; j < stars.length; j++) {
            stars[j].setText(j <= upTo ? filled : "☆");
            stars[j].setStyle("-fx-font-size: 30px; -fx-text-fill: " + (j <= upTo ? color : "gray") + "; -fx-cursor: hand;");
        }
    }

    private static void showWarning(Stage owner) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please select a rating before submitting.");
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
