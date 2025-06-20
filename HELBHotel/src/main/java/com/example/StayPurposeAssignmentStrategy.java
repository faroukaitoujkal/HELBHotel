package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StayPurposeAssignmentStrategy implements RoomAssignmentStrategy {

    @Override
    public String assignRoom(Hotel hotel, Reservation reservation) {
        RoomType targetType;

        if (reservation.stayPurpose == StayPurpose.BUSINESS) {
            targetType = RoomType.BUSINESS;
        } else if (reservation.stayPurpose == StayPurpose.TOURISM || reservation.stayPurpose == StayPurpose.OTHER) { // si le motif est tourism OU autre
            if (!reservation.isSmoker && reservation.numberOfChildren == 0) { // non-fumeur ET sans enfants
                targetType = RoomType.LUXURY;
            } else {
                targetType = RoomType.ECONOMIC; // cas contraire
            }
        } else {
            return null; 
        }

        List<Room> shuffledRooms = new ArrayList<>(hotel.getAllRooms()); // on crée une nouvelle liste dans laquelle on va mettre toutes les chambres de l'hôtel
        Collections.shuffle(shuffledRooms); 
        
        for (Room room : shuffledRooms) { // on itére sur la liste des chambres melangées pour que lorsque l'utilisateur refresh la proposition on lui repropose pas la même chambre
            if (!room.isReserved() && room.getType() == targetType) { // on retourne l'id de la chambre si elle n'est pas réservée ET que son type correspond à notre targetType
                return room.getId();
            }
        }

        return null;
    }
}
