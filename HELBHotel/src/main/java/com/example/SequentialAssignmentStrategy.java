package com.example;

import java.util.Map;

public class SequentialAssignmentStrategy implements RoomAssignmentStrategy {
    
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

        for (Floor floor : hotel.getFloors()) { // getFloors() retourne la liste des étages
            for (Map.Entry<String, Room> entry : floor.getRooms().entrySet()) {
                Room room = entry.getValue();
                if (!room.isReserved() && room.getType() == targetType) {
                    return room.getId();
                }
            }
        }

        return null; // aucune chambre disponible
    }
}
