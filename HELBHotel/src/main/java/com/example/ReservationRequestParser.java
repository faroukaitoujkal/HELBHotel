package com.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReservationRequestParser {

    private final List<ReservationRequest> reservationRequestList = new ArrayList<>();
    private final List<String> invalidLines = new ArrayList<>();
    private final String sourceFilename;
    private int lastParsedLine = 0;

    public ReservationRequestParser(String filename) {
        this.sourceFilename = filename;
        parseAll();
    }

    public List<ReservationRequest> getAllValidRequests() {
        return new ArrayList<>(reservationRequestList);
    }

    private void parseAll() {
        try (Scanner scanner = new Scanner(new File(sourceFilename))) {
            int lineIndex = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                if (isLineCorrect(line)) { // si la ligne est correcte on crée une nouvelle demande de réservation avec ses infos
                    reservationRequestList.add(getReservationRequestFromLine(line));
                } else {
                    invalidLines.add(line); // si la ligne est invalide on l'ajoute à notre liste de lignes invalides
                }
                lineIndex++;
            }
            lastParsedLine = lineIndex;
        } catch (IOException e) {
            System.err.println("Error reading reservation file: " + e.getMessage());
        }
    }

    public List<ReservationRequest> parseNewLines() {
        List<ReservationRequest> newRequests = new ArrayList<>();

        invalidLines.clear();

        try (Scanner scanner = new Scanner(new File(sourceFilename))) {
            int currentLine = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (currentLine >= lastParsedLine) {
                    if (isLineCorrect(line)) {
                        ReservationRequest request = getReservationRequestFromLine(line);
                        reservationRequestList.add(request);
                        newRequests.add(request);
                    } else {
                        invalidLines.add(line);
                    }
                }
                currentLine++;
            }

            lastParsedLine = currentLine;
            
        } catch (IOException e) {
            System.err.println("Error reading new reservation lines: " + e.getMessage());
        }

        return newRequests;
    }

    private ReservationRequest getReservationRequestFromLine(String line) {
        String[] parts = line.split(",");
        String firstName = parts[0].trim();
        String lastName = parts[1].trim();
        int numberOfPeople = Integer.parseInt(parts[2].trim());
        boolean isSmoker = parts[3].trim().equalsIgnoreCase("Fumeur");
        StayPurpose stayPurpose = StayPurpose.fromString(parts[4].trim());
        int numberOfChildren = Integer.parseInt(parts[5].trim());

        Reservation reservation = new Reservation(
                firstName, lastName, numberOfPeople, isSmoker, stayPurpose, numberOfChildren
        );

        return new ReservationRequest(reservation, null);
    }

    private boolean isLineCorrect(String line) {
        String[] parts = line.split(","); // on sépare la ligne en 6 parties différentes grâce au ";" déja présent
        if (parts.length != 6) return false; // si il n'y a pas exactement 6 parties la ligne est invalide

        try {
            int numberOfPeople = Integer.parseInt(parts[2].trim()); // on récupere le nombre de personnes 
            int numberOfChildren = Integer.parseInt(parts[5].trim());

            if (numberOfPeople < 1 || numberOfPeople > 4) return false; // si le nombre de personne est + élevée que 4 ou moins que 1 la ligne est invalide
            if (numberOfChildren < 0 || numberOfChildren >= numberOfPeople) return false; // si le nombre d'enfants est plus grand ou égale au nombre de personnes la ligne est invalide car il faut au moins 1 adulte pour une réservation valide

            String smoker = parts[3].trim().toLowerCase();
            if (!smoker.equals("fumeur") && !smoker.equals("non-fumeur")) return false; // si la partie smoker n'est pas égale à fumeur ou non-fumeur la ligne est invalide

            String purpose = parts[4].trim().toLowerCase();
            return purpose.equals("tourisme") || purpose.equals("affaire") || purpose.equals("autre"); // le motif du séjour doit être un des 3 choix possibles sinon la ligne est invalide

        } catch (NumberFormatException e) {
            return false;
        }
    }

    // méthode qui supprime les lignes valides du fichier reservation.csv et ne garde que les invalides
    public void removeValidReservationsFromFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(sourceFilename, false))) {
            for (String line : invalidLines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing filtered reservation file: " + e.getMessage());
        }
    }
}