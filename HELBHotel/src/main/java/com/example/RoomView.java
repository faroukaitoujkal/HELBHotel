package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RoomView {

    public static String display(ReservationRequest request, Hotel hotel) {
        final String[] result = {null};

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Room Reservation");

        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(10));
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.getChildren().addAll(
            new Label("Last Name: " + request.reservation.lastName),
            new Label("First Name: " + request.reservation.firstName),
            new Label("Number of Children: " + request.reservation.numberOfChildren),
            new Label("Number of People: " + request.reservation.numberOfPeople),
            new Label("Smoker: " + (request.reservation.isSmoker ? "Yes" : "No")),
            new Label("Purpose of Stay: " + request.reservation.stayPurpose)
        );

        TextField proposedRoomField = new TextField(request.roomId);
        proposedRoomField.setPrefWidth(100);

        Button confirmButton = new Button("Confirm");
        confirmButton.setDisable(!isRoomAvailable(hotel, proposedRoomField.getText()));

        proposedRoomField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(!isRoomAvailable(hotel, newVal));
        });

        confirmButton.setOnAction(e -> {
            result[0] = proposedRoomField.getText();
            window.close();
        });

        VBox rightBox = new VBox(10);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.getChildren().addAll(new Label("Suggestion"), proposedRoomField, confirmButton);

        HBox layout = new HBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(infoBox, rightBox);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return result[0];
    }    

    public static boolean displayInfo(Room room) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Room Details: " + room.getId());
        window.setMinWidth(300);
    
        Label infoLabel = new Label();
        Button releaseRoomButton = new Button("Release Room");
    
        if (room.isReserved()) {
            infoLabel.setText(room.toString());
            releaseRoomButton.setDisable(false);
        } else {
            infoLabel.setText("This room is free.");
            releaseRoomButton.setDisable(true);
        }
    
        final boolean[] cancelled = {false};
    
        releaseRoomButton.setOnAction(e -> {
            boolean confirmed = ConfirmFreeRoomBoxView.display(); // on récupère le résultat de la méthode display() -> true = confirmer la libération de la chambre
    
            if (confirmed) {
                cancelled[0] = true;
                window.close();
    
                // nn lance la fênetre pour la note du client
                javafx.application.Platform.runLater(() -> {
                    int rating = RatingView.askForRating();    
                    Ticket ticket = TicketFactory.generateTicket(room, rating);
                    ticket.play();
                });
            }
        });
    
        VBox layout = new VBox(10);
        layout.getChildren().addAll(infoLabel, releaseRoomButton);
        layout.setAlignment(Pos.CENTER);
    
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    
        return cancelled[0];
    }        
    
    private static boolean isRoomAvailable(Hotel hotel, String roomId) {
        Room room = hotel.getRoom(roomId);
        return room != null && !room.isReserved();
    }                    
}
