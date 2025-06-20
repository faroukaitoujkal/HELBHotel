package com.example;

import java.util.Random;

/**
 * Classe qui se charge de générer le code de réduction
 */
public class DiscountCodeGenerator {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 10;

    private static final char GOLD_PREFIX = 'G';
    private static final char SILVER_PREFIX = 'S';
    private static final char BRONZE_PREFIX = 'B';
    private static final char UNKNOWN_PREFIX = 'X';

    private static final int GOLD_DISCOUNT = 100;
    private static final int SILVER_DISCOUNT = 50;
    private static final int BRONZE_DISCOUNT = 25;

    public static String generateCode(int discountPercentage) {
        StringBuilder code = new StringBuilder(); // ici on utilise un StringBuilder car on va ajouter des caractéres à notre chaine de maniére progressive
        Random random = new Random();

        char prefix;
        switch (discountPercentage) {
            case GOLD_DISCOUNT:
                prefix = GOLD_PREFIX; // gold
                break;
            case SILVER_DISCOUNT:
                prefix = SILVER_PREFIX; // silver
                break;
            case BRONZE_DISCOUNT:
                prefix = BRONZE_PREFIX; // bronze
                break;
            default:
                prefix = UNKNOWN_PREFIX; // inconnu
        }

        code.append(prefix); // on ajoute le caractére représentant le pourcentage de réduction en début de chaine

        // on ajoute ensuite les 9 derniers caractères aléatoires
        for (int i = 1; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return code.toString(); // on retourne le code de réduction complet 
    }
}
