package com.example;

public interface RoomAssignmentStrategy {
    /**
     * Propose une chambre pour la réservation donnée
     */
    String assignRoom(Hotel hotel, Reservation reservation);
}
