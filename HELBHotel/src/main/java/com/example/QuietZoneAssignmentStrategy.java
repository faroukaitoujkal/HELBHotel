package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuietZoneAssignmentStrategy implements RoomAssignmentStrategy {

    @Override
    public String assignRoom(Hotel hotel, Reservation reservation) {
        List<Room> validRooms = new ArrayList<>();

        for (Room room : hotel.getAllRooms()) {
            if (room.isReserved()) {
                continue;
            }
            
            // si fumeur
            if (reservation.isSmoker) {
                continue;
            }

            // clients avec enfants
            if (reservation.numberOfChildren > 0) {
                continue;
            }

            validRooms.add(room);
        }

        if (!validRooms.isEmpty()) {
            Room chosen = validRooms.get(new Random().nextInt(validRooms.size()));
            return chosen.getId();
        }

        // on retourne null si aucune chambre ne respecte les critères
        return null;
    }
}
