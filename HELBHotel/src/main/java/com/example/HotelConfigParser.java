package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui s'occupe de parser le fichier .hconfig pour la configuration de notre hôtel
 */
public class HotelConfigParser {

    public static String[][][] loadHotelConfig(String filename) {
        int numberOfFloors = 0;
        List<String[]> layout = new ArrayList<>(); // représente la disposition de base d'un étage
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
    
            if (line == null || line.trim().isEmpty()) { // si la premiére ligne est vide on arrête le programme avec une exception IllegalArgumentException
                throw new IllegalArgumentException("Fichier .hconfig vide ou invalide");
            }
    
            numberOfFloors = Integer.parseInt(line.trim()); // on récupére le nombre d'étages
    
            while ((line = reader.readLine()) != null) { // on lit le reste de la disposition des chambres 
                if (!line.trim().isEmpty()) {
                    String[] parts = line.trim().split(","); // chaque ligne de notre fichier devient un tableau de String
                    layout.add(parts); // et finalement on stocke chaque ligne dans notre layout
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erreur de lecture du fichier .hconfig : " + e.getMessage());
            throw new RuntimeException("Erreur lecture .hconfig", e);
        }
    
        if (layout.isEmpty()) {
            throw new IllegalArgumentException("Disposition de base vide dans .hconfig");
        }
    
        int rowCount = layout.size();
        int colCount = layout.get(0).length;
        String[][][] config = new String[numberOfFloors][rowCount][colCount]; // on crée un tableau à 3 dimensions qui va nous permettre de récupéree chaque type de chambre dans notre hôtel
    
        // on duplique le même layout pour chaque étage
        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < rowCount; row++) {
                System.arraycopy(layout.get(row), 0, config[floor][row], 0, colCount); // on copie une ligne entiére à la fois
            }
        }
    
        return config;
    }    
}
