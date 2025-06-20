package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DiscountCodeVerificationView {
    
    public static void display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Code Verification");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label instruction = new Label("Enter the code (10 characters)");
        TextField codeInput = new TextField();
        codeInput.setPrefWidth(150);

        Label resultLabel = new Label("No valid code currently available");
        ImageView statusIcon = new ImageView(new Image(DiscountCodeVerificationView.class.getResourceAsStream("/images/red_cross.png")));
        statusIcon.setFitWidth(40);
        statusIcon.setFitHeight(40);

        HBox inputBox = new HBox(10, instruction, codeInput);
        inputBox.setAlignment(Pos.CENTER);

        HBox resultBox = new HBox(10, resultLabel, statusIcon);
        resultBox.setAlignment(Pos.CENTER);

        codeInput.setOnAction(e -> {
            String enteredCode = codeInput.getText().trim();
            if (DiscountCodeVerification.isValidCode(enteredCode)) { // on vérifie si le code est valide ou pas
                int discount = DiscountCodeVerification.decode(enteredCode); // on décode le code pour connaître le taux de pourcentage de la réduction
                resultLabel.setText(discount + "% discount");
                statusIcon.setImage(new Image(DiscountCodeVerificationView.class.getResourceAsStream("/images/green_check.png")));
            } else {
                resultLabel.setText("No valid code currently available");
                statusIcon.setImage(new Image(DiscountCodeVerificationView.class.getResourceAsStream("/images/red_cross.png")));
            }
        });

        layout.getChildren().addAll(inputBox, resultBox);

        Scene scene = new Scene(layout, 400, 200);
        window.setScene(scene);
        window.show();
    }
}
