package com.example;

/**
 * Classe qui se charge des vérifications sur les codes de réductions
 */
public class DiscountCodeVerification {

    private static final int CODE_LENGTH = 10;
    private static final char GOLD_PREFIX = 'G';
    private static final char SILVER_PREFIX = 'S';
    private static final char BRONZE_PREFIX = 'B';

    private static final int GOLD_DISCOUNT = 100;
    private static final int SILVER_DISCOUNT = 50;
    private static final int BRONZE_DISCOUNT = 25;
    private static final int NO_DISCOUNT = 0;

    public static boolean isValidCode(String code) {
        if (code == null || code.length() != CODE_LENGTH) {
            return false; // si le code est null ou si sa longueur ne correspond pas à 10 on retourne false
        } 

        char prefix = code.charAt(0); // on récupére le premier caractére du code
        return prefix == 'G' || prefix == 'S' || prefix == 'B'; // doit commencer par G, S ou B pour être valide    
    }

    public static int decode(String code) {
        char discountMarker = code.charAt(0); // on extrait seulement le premier caractére du code en paramétre pour le décoder et savoir le nombre de pourcentage de réduction qu'il octroie
        switch (discountMarker) {
            case GOLD_PREFIX:
                return GOLD_DISCOUNT;
            case SILVER_PREFIX:
                return SILVER_DISCOUNT;
            case BRONZE_PREFIX:
                return BRONZE_DISCOUNT;
            default:
                return NO_DISCOUNT;
        }
    }
}
