package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class HotelController {

    private Hotel hotel;
    private HotelView view;
    private final String simulationDataFilename = "src/main/java/com/example/reservation.csv";
    private final String configFilename = "src/main/java/com/example/.hconfig";
    private ReservationRequestParser parser;
    private RoomAssignmentStrategy strategy;

    public HotelController(HotelView view) {
        this.view = view; 

        String[][][] config = HotelConfigParser.loadHotelConfig(configFilename); // on charge la config de l'hôtel
        hotel = new Hotel(config, view);
        strategy = new RandomAssignmentStrategy();
        parser = new ReservationRequestParser(simulationDataFilename);

        view.setHotel(hotel);

        loadAndDisplayPendingReservations(parser.getAllValidRequests());

        parser.removeValidReservationsFromFile();
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(10), e -> { // chaque 10 secondes on va parser le fichier de reservation.csv et récupérer les nouvelles reqûetes valides
                List<ReservationRequest> newRequests = parser.parseNewLines(); // on retourne la liste avec toutes les nouvelles résèrvations
                loadAndDisplayPendingReservations(newRequests);
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void loadAndDisplayPendingReservations(List<ReservationRequest> requests) {
        for (ReservationRequest request : requests) {
            String assignedRoomId = strategy.assignRoom(hotel, request.reservation); // en fonction des critéres de la résèrvation notre stratégie va s'occuper de retourner un ID de chambre adéquat
            if (assignedRoomId != null) {
                request.roomId = assignedRoomId;
                javafx.application.Platform.runLater(() -> view.addPendingReservationCard(request, this));
            } else {
                System.out.println("Aucune chambre disponible pour : " + request.reservation.getLastName());
            }
        }
    }    

    public void sortPendingReservations(String criteria) {
        List<ReservationRequest> sorted = new ArrayList<>(view.getPendingReservations()); // on récupere les réservations en attente

        Comparator<ReservationRequest> comparator;
        if ("Name".equals(criteria)) { // si le tri est sur le champ Name
            comparator = Comparator.comparing(r -> r.reservation.getLastName().toLowerCase().trim());
        } else {
            comparator = Comparator.comparingInt(r -> { // sinon on trie par numéro de chambre
                String roomId = r.roomId;

                char floorLetter = roomId.charAt(0);
                int floorIndex = floorLetter - 'A';
        
                String numberPart = roomId.substring(1, roomId.length() - 1); // on extrait le numéro de chambre au millie entre l'étage et le type
                int roomNumber;
                try {
                    roomNumber = Integer.parseInt(numberPart); // on le parse
                } catch (NumberFormatException e) {
                    roomNumber = Integer.MAX_VALUE; // en cas d'erreur on place la réservation à la fin tout en bas
                }
        
                return floorIndex * 1000 + roomNumber; // On combine étage et numéro pour trier dans l'ordre étage → chambre
            });
        }        

        sorted.sort(comparator); // on applique le tri à notre liste
        javafx.application.Platform.runLater(() -> view.displaySortedReservations(sorted)); // on met à jour l'UI
    }

    /**
     * Lorsqu'on clique sur une réservation pour la traiter
     */
    public void handleReservation(ReservationRequest request) {
        String chosenRoomId = RoomView.display(request, hotel); // on affiche la réservation et on attend la réponse soite l'id de la chambre soite null si l'utilisateur a fermé la pop up
        if (chosenRoomId != null) { // si l'utilisateur a confirmé la chambre
            request.roomId = chosenRoomId;
            hotel.reserveRoom(request); // on réserve la chambre
            view.removePendingReservationCard(request); // on supprime la carte de l'UI après confirmation de la chambre
        }
    }

    public void changeStrategy(String strategyName) {
        switch (strategyName) {
            case "Random Assignment":
                strategy = new RandomAssignmentStrategy();
                break;
            case "Quiet Zone":
                strategy = new QuietZoneAssignmentStrategy();
                break;
            case "Stay Purpose":
                strategy = new StayPurposeAssignmentStrategy();
                break;
            case "Sequential Assignment":
                strategy = new SequentialAssignmentStrategy();
                break;
            default:
                strategy = new StayPurposeAssignmentStrategy();
        }
    }    
    
    public void refreshReservation(ReservationRequest req) {
        String newRoomId = strategy.assignRoom(hotel, req.reservation); // on demande une nouvelle chambre selon la stratégie sélectionnée
    
        if (newRoomId != null) {
            req.roomId = newRoomId;
    
            javafx.application.Platform.runLater(() -> view.updatePendingReservationCard(req)); // on dit au programme de mettre à jour l'UI dans le thread javafx sans cette précision ça une erreur survient
        } else {
            System.out.println("Aucune chambre disponible pour " + req.reservation.getLastName());
        }
    }            
}
