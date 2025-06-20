package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un hôtel composé de plusieurs étages et de chambres
 */
public class Hotel implements RoomObserver {

    private final int rows;
    private final int columns;
    private final String[][] baseLayout;
    private final List<Floor> floors;
    private final HotelObserver hotelObserver;

    public Hotel(String[][][] config, HotelObserver hotelObserver) {
        this.rows = config[0].length;
        this.columns = config[0][0].length;
        this.baseLayout = config[0]; // disposition de base
        this.hotelObserver = hotelObserver;
        this.floors = new ArrayList<>();
        initializeFloorsAndRooms(config); // à la création de l'objet Hotel on génère toutes les chambres 
    }

    private void initializeFloorsAndRooms(String[][][] config) {
        for (int floorIndex = 0; floorIndex < config.length; floorIndex++) {
            String floorLetter = getLetterFromIndex(floorIndex);
            Floor floor = new Floor(floorLetter); // on crée un étage avec la lettre correspondante
    
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    String typeCode = config[floorIndex][row][col]; // E, B, L ou Z
    
                    int roomNumber = row * columns + col + 1; // formule qui permet d'avoir les nombres de notre grille
                    String roomId = floorLetter + roomNumber + typeCode;
    
                    Room room = new Room(typeCode, roomId);
    
                    if (room.getType() != RoomType.EMPTY) {
                        room.addObserver(this); // on observe seulement les chambres actives
                    }
    
                    floor.addRoom(room); // on ajoute même les chambres vides pour garder la structure
                }
            }
    
            floors.add(floor);
        }
    }    
    
    public Room getRoom(String roomId) {
        for (Floor floor : floors) {
            Room room = floor.getRooms().get(roomId);
            if (room != null) return room;
        }
        return null;
    }

    public boolean isRoomReserved(String roomId) {
        Room room = getRoom(roomId);
        if (room != null && room.isReserved()) {
            return true;
        }
        return false;
    }

    public void reserveRoom(ReservationRequest request) {
        Room room = getRoom(request.roomId);
        if (room != null) {
            room.reserveRoom(request.reservation);
        }
    }

    public void freeRoom(String roomId) {
        Room room = getRoom(roomId);
        if (room != null) {
            room.freeRoom();
        }
    }

    // Méthode appelée automatiquement lorsqu'une Room notifie un changement -> notifyObservers();
    @Override
    public void onRoomUpdated(Room room) {
        if (room.isReserved()) {
            hotelObserver.reserveRoom(room.getId());
        } else {
            hotelObserver.freeRoom(room.getId());
        }
    }

    // Convertit un index d'étage en lettre (0 -> A, 1 -> B, ...)
    public String getLetterFromIndex(int index) {
        return String.valueOf((char) ('A' + index));
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public Floor getFloor(String letter) {
        for (Floor floor : floors) {
            if (floor.getLetter().equals(letter)) return floor;
        }
        return null;
    }

    public String[][] getBaseLayout() {
        return baseLayout;
    }

    public List<Room> getAllRooms() {
        List<Room> allRooms = new ArrayList<>();
        for (Floor floor : floors) { // on parcourt tout les étages de notre hotel 
            allRooms.addAll(floor.getRooms().values()); // on récupère toutes les valeurs de la Map de Room donc les objets Room et on les ajoute à notre liste de rooms
        }
        return allRooms;
    }

    public int getNumberOfFloors() {
        return floors.size();
    }
    
    public int getNumberOfColumns() {
        return columns;
    }
}
