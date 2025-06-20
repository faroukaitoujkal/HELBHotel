package com.example;

public class TicketFactory {
    
    public static Ticket generateTicket(Room room, int rating) {
        RoomType type = room.getType();
        double chance = calculateChance(type, rating);
        double randomNumber = Math.random(); // random entre 0.00 et 1

        if (randomNumber < chance) { // si le nombre aléatoire tiré est inférieur à la chance calculée le client obtient un gold ticket
            int floorNumber = extractFloorNumber(room);
            return new GoldTicket(floorNumber);
        } else if (randomNumber < chance + 0.3) { // si le nombre aléatoire tiré est inférieur à la chance calculée + 0,3 silver ticket 
            return new SilverTicket();
        } else { // sinon bronze ticket
            return new BronzeTicket();
        }
    }

    private static int extractFloorNumber(Room room) {
        char floorChar = room.getId().charAt(0); // on extrait seulement la premiére lettre qui représente l'étage A->1 .....
        return floorChar - 'A' + 1; // on utilise les valeurs numériques (ASCII) des lettres A=65, B=66 etc...
    }

    private static double calculateChance(RoomType type, int rating) {
        int luxuryBonus = getLuxuryBonus(type);
        double chance = (luxuryBonus + rating) / 10.0; // + ta une chambre luxueuse et + ta mis un bon avis + ta de chance de gagner
        return Math.min(chance, 1.0);
    }
    
    private static int getLuxuryBonus(RoomType type) {
        switch (type) {
            case LUXURY:
                return 3;
            case BUSINESS:
                return 2;
            case ECONOMIC:
            default:
                return 1;
        }
    }    
}
