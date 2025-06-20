package com.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GoldTicket implements Ticket {

    private final int floorNumber;

    public GoldTicket(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public void play() {
        int totalDoors = floorNumber + 2;

        int winningDoor = new Random().nextInt(totalDoors) + 1; // on définit la porte gagnante aléatoire entre un random de 1 et totalDoors
        int userChoice = askUserToChooseDoor(totalDoors); // on récupére la porte choisie par l'utilisateur

        boolean win = userChoice == winningDoor; // si la porte que l'utilisateur a choisi est la porte gagnante il gagne

        Alert result = new Alert(Alert.AlertType.INFORMATION);
        result.setTitle("Gold Ticket");
        result.setHeaderText("Gold Game");
        result.setContentText(win
            ? "You won! 100% discount. Code: " + DiscountCodeGenerator.generateCode(100)
            : "You lost! The correct door was: " + winningDoor);
        result.showAndWait();
    }

    private int askUserToChooseDoor(int max) {
        List<String> options = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            options.add("Door " + i);
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle("Gold Ticket");
        dialog.setHeaderText("Choose a door (1 to " + max + ")");
        dialog.setContentText("Your choice:");

        Optional<String> result = dialog.showAndWait();
        return result.map(s -> Integer.parseInt(s.replaceAll("[^0-9]", ""))).orElse(1);
    }
}
