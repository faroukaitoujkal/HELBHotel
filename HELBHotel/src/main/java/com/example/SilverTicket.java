package com.example;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import java.util.Random;

public class SilverTicket implements Ticket {
    
    private static final String[] WORDS = {"strategy", "singleton", "observer", "factory"};

    @Override
    public void play() {
        String word = WORDS[new Random().nextInt(WORDS.length)]; // on récupére un mot au hasard de nore tableau de mots en faisant un random sur les indexs de notre tableau
        String shuffled = shuffle(word);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Silver Ticket");
        dialog.setHeaderText("Guess the word (1 attempt only)");
        dialog.setContentText("Scrambled word: " + shuffled);

        Optional<String> input = dialog.showAndWait();
        boolean win = input.isPresent() && input.get().equalsIgnoreCase(word); // on compare le mot de l'utilisateur au mot à deviner tout en étant insensible à la casse

        Alert result = new Alert(Alert.AlertType.INFORMATION);
        result.setTitle("Result");
        result.setContentText(win ? "Won! 50% discount. Code: " + DiscountCodeGenerator.generateCode(50)
                                  : "Lost! The word was: " + word);
        result.showAndWait();
    }

    private String shuffle(String input) {
        char[] chars = input.toCharArray(); // on convertit notre String en tableau de char 
        Random rnd = new Random();
        for (int i = 0; i < chars.length; i++) {
            int j = rnd.nextInt(chars.length);
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars); // on return un String représentant le mot aprés le shuffle
    }
}
