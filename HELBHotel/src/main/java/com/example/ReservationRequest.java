package com.example;

/**
 * Classe représentant une requête de réservation
 */
public class ReservationRequest {
    
    public Reservation reservation;
    public String roomId;

    public ReservationRequest(Reservation reservation, String roomId) {
        this.reservation = reservation;
        this.roomId = roomId;
    }
}
