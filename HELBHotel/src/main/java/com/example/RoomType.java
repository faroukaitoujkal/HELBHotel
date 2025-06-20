package com.example;

/**
 * Enumération représentant les types de chambres dans l'hôtel. 
 */
public enum RoomType {
    ECONOMIC("E"),
    BUSINESS("B"),
    LUXURY("L"),
    EMPTY("Z"); // type spécial pour les cases vides

    private final String code;

    RoomType(String code) {
        this.code = code;
    }

    // convertit les typeCode en un enum de RoomType 
    public static RoomType fromCode(String code) {
        switch (code.toUpperCase()) {
            case "E":
                return ECONOMIC;
            case "B":
                return BUSINESS;
            case "L":
                return LUXURY;
            case "Z":
                return EMPTY;
            default:
                throw new IllegalArgumentException("Invalid room type code: " + code);
        }
    }
}
