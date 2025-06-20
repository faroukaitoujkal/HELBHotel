package com.example;

/**
 * Classe représentant une réservation vide
 */
public class EmptyReservation extends Reservation {

    private static EmptyReservation instance;

    private EmptyReservation() { // constructeur en private pour éviter qu'on puisse en créer d'autre
        super("/", "/", 1, false, StayPurpose.OTHER, 0);
    }

    public static EmptyReservation getInstance() {
        if (instance == null) { // on s'assure qu'il n'existe aucune instance de EmptyReservation avant d'en créer une c'est ce qu'on apelle le Singleton
            instance = new EmptyReservation();
        }
        return instance; // on retourne l'unique instance qui sera partagé dans tout le programme
    }
}
