package com.example;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Classe qui représente un étage dans l'hôtel
 */
public class Floor {

    private String letter;
    private Map<String, Room> rooms;

    public Floor(String letter) {
        this.letter = letter;
        this.rooms = new LinkedHashMap<>(); // on initialise une LinkedHashMap pour stocker les chambres de l'étage
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room); // la clé est l'id de la room (ex: A15B) et la valeur est l'objet room passé en paramétre 
    }

    public Map<String, Room> getRooms() {
        return rooms; // retourne toute la map des rooms
    }

    public String getLetter() {
        return letter;
    }
}
