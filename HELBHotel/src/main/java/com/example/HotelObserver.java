package com.example;

/**
 * Interface pour notifier la vue des changements dans les chambres de l'hôtel
 */
public interface HotelObserver {
    void reserveRoom(String roomId);
    void freeRoom(String roomId);
}
