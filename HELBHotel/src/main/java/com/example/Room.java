package com.example;

import java.util.ArrayList;
import java.util.List;

public class Room {
    
    private final RoomType type;
    private final String id;
    private boolean reserved;
    private Reservation reservation;
    private final List<RoomObserver> observers = new ArrayList<>();

    public Room(String typeCode, String id) {
        this.type = RoomType.fromCode(typeCode); // par exemple convertit le typeCode 'E' en un enum RoomType.ECONOMIC     
        this.id = id;
        this.reserved = false;
        this.reservation = EmptyReservation.getInstance(); // pour éviter d'avoir un NullPointerException quand la chambre n'est pas réservè et on fait un room.getReservation()
    }

    public void reserveRoom(Reservation reservation) {
        this.reserved = true;
        this.reservation = reservation;
        notifyObservers(); // on notifie l'observeur (Hotel) du changement d'état de la room (état réservé)
    }

    public void freeRoom() {
        this.reserved = false;
        this.reservation = EmptyReservation.getInstance(); // on affecte la chambre à une résèrvation vide
        notifyObservers(); // on notifie l'observeur (Hotel) du changement d'état de la room (état libre)
    }

    public boolean isReserved() {
        return reserved;
    }

    public RoomType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void addObserver(RoomObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    private void notifyObservers() {
        for (RoomObserver observer : observers) {
            observer.onRoomUpdated(this);
        }
    }

    @Override
    public String toString() {
        return "Room " + id + " (" + type.toString() + ")\n" + reservation.toString();
    }
}
