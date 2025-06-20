package com.example;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class BronzeTicket implements Ticket {
    
    @Override
    public void play() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bronze Ticket");
        alert.setHeaderText("Door Game (choose between 2 doors)");

        int winningDoor = (int) (Math.random() * 2) + 1; // random entre 1 et 2

        TextInputDialog input = new TextInputDialog(); 
        input.setTitle("Choose a Door");
        input.setHeaderText("Door 1 or 2?");

        Optional<String> choice = input.showAndWait(); // on récupére le résultat de l'input de l'user (Optional pour éviter les NullPointerException)
        boolean win = choice.isPresent() && choice.get().trim().equals(String.valueOf(winningDoor)); // on vérifie si il a cliqué sur ok ET si son choix correspond à la winningDoor ou pas pour définir la win

        alert.setContentText(
            win ? "You won! 25% discount. Code: " + DiscountCodeGenerator.generateCode(25) // si win est égale à true on génére un code promo de 25% sinon on affiche un message de défaite
                : "You lost! Maybe next time."
        );
        alert.showAndWait();
    }
}
