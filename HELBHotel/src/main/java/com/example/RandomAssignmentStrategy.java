package com.example;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomAssignmentStrategy implements RoomAssignmentStrategy {
    
    private final Random random = new Random();

    @Override
    public String assignRoom(Hotel hotel, Reservation reservation) {
        List<Room> availableRooms = hotel.getAllRooms().stream() // on récupére de notre liste de chambres seulement ceux qui ne sont pas réservées et on les stockes dans notre list availableRooms
                .filter(room -> !room.isReserved()) 
                .filter(room -> room.getType() != RoomType.EMPTY) // on exclut les chambres vides
                .collect(Collectors.toList());

        if (availableRooms.isEmpty()) {
            return null; // si notre liste est vide...
        }

        Room selectedRoom = availableRooms.get(random.nextInt(availableRooms.size())); // on affecte une chambre au hasard de notre liste de chambres disponibles à notre chambre selectedRoom
        return selectedRoom.getId(); // on retourne son id
    }
}
