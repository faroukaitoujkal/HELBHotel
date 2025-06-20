package com.example;

/**
 * Enum représentant le motif du séjour d'un client
 */
public enum StayPurpose {
    TOURISM,
    BUSINESS,
    OTHER;

    // convertit une chaîne de caractères vers un StayPurpose
    public static StayPurpose fromString(String value) {
        switch (value.trim().toLowerCase()) { // on transforme toute la chaine de caractére en miniscule pour que la solution soit insensible à la casse et ensuite on fait un switch case pour donner le bon enum à chaque valeur
            case "tourisme":
                return TOURISM;
            case "affaire":
                return BUSINESS;
            case "autre":
            default:
                return OTHER;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case TOURISM:
                return "Tourism";
            case BUSINESS:
                return "Business";
            case OTHER:
            default:
                return "Other";
        }
    }
}
