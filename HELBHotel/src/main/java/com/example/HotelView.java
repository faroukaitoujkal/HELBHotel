package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class HotelView implements HotelObserver {

    private final int winHeight = 600;
    private final int winWidth = 900;

    private final Stage stage;
    private Scene scene;

    private VBox leftPanel;
    private VBox floorLayout;
    private VBox reservationList;
    private ScrollPane reservationScroll;

    private ComboBox<String> floorSelector;
    private ComboBox<String> strategySelector;
    private ComboBox<String> sortSelector;

    private Hotel hotel;
    private HotelController controller;

    private final Map<String, Button> roomButtonsMap = new HashMap<>();
    private final List<ReservationRequest> pendingReservations = new ArrayList<>();

    public HotelView(Stage stage) {
        this.stage = stage;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        initView();
    }

    @Override
    public void reserveRoom(String roomId) {
        updateRoomColor(roomId);
        Room room = hotel.getRoom(roomId);
        if (room != null && room.isReserved()) {
            addReservationCard(room);
        }
    }

    @Override
    public void freeRoom(String roomId) {
        updateRoomColor(roomId);
        removeReservationCard(roomId);
    }

    public void addPendingReservationCard(ReservationRequest request, HotelController controller) {
        this.controller = controller;
        pendingReservations.add(request);
        controller.sortPendingReservations(sortSelector.getValue());
    }

    public void removePendingReservationCard(ReservationRequest request) {
        pendingReservations.remove(request);
        displaySortedReservations(getPendingReservations());
    }

    public void updatePendingReservationCard(ReservationRequest req) {
        removePendingReservationCard(req);
        addPendingReservationCard(req, controller);
    }

    public void displaySortedReservations(List<ReservationRequest> sortedRequests) {
        reservationList.getChildren().clear();

        for (ReservationRequest req : sortedRequests) {
            VBox card = new VBox(5);
            card.setPadding(new Insets(10));
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: " + getColor(hotel.getRoom(req.roomId).getType()) + "; -fx-background-radius: 10;");
            card.setUserData(req);

            String shortName = req.reservation.getFirstName().charAt(0) + ". " + req.reservation.getLastName();
            Label nameLabel = new Label(shortName);
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Label roomLabel = new Label(req.roomId);
            roomLabel.setStyle("-fx-font-size: 12px;");

            VBox infoBox = new VBox(3, nameLabel, roomLabel);
            infoBox.setAlignment(Pos.CENTER);

            card.getChildren().add(infoBox);
            card.setOnMouseClicked(e -> controller.handleReservation(req));

            Image image = new Image(getClass().getResourceAsStream("/images/refresh.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(16);
            imageView.setFitHeight(16);

            Button refreshButton = new Button();
            refreshButton.setGraphic(imageView);
            refreshButton.setStyle("-fx-background-radius: 50%; -fx-padding: 5;");
            refreshButton.setOnAction(e -> controller.refreshReservation(req));

            HBox container = new HBox(10, card, refreshButton);
            container.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(card, Priority.ALWAYS);
            container.setPadding(new Insets(0));

            reservationList.getChildren().add(container);
        }
    }

    public List<ReservationRequest> getPendingReservations() {
        return new ArrayList<>(pendingReservations);
    }

    private void initView() {
        leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setAlignment(Pos.TOP_CENTER);

        floorSelector = new ComboBox<>();
        for (Floor floor : hotel.getFloors()) {
            floorSelector.getItems().add(floor.getLetter() + "1");
        }
        floorSelector.getSelectionModel().selectFirst();
        floorSelector.setOnAction(e -> updateDisplayedFloor(floorSelector.getValue().substring(0, 1)));

        floorLayout = new VBox(5);
        floorLayout.setAlignment(Pos.CENTER);
        VBox.setVgrow(floorLayout, Priority.ALWAYS);

        VBox legend = buildLegend();

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(new Label("Floor : "), floorSelector);

        leftPanel.getChildren().addAll(legend, topBar, floorLayout);

        reservationList = new VBox(10);
        reservationList.setPadding(new Insets(10));
        reservationList.setAlignment(Pos.TOP_CENTER);

        reservationScroll = new ScrollPane(reservationList);
        reservationScroll.setFitToWidth(true);
        reservationScroll.setPrefWidth(300);
        reservationScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        reservationScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(reservationScroll, Priority.ALWAYS);

        strategySelector = new ComboBox<>();
        strategySelector.getItems().addAll(
            "Random Assignment",
            "Quiet Zone",
            "Stay Purpose",
            "Sequential Assignment"
        );
        strategySelector.getSelectionModel().select("Random Assignment");
        strategySelector.setOnAction(e -> {
            if (controller != null) {
                controller.changeStrategy(strategySelector.getValue());
            }
        });
        strategySelector.setMaxWidth(Double.MAX_VALUE);

        sortSelector = new ComboBox<>();
        sortSelector.getItems().addAll("Name", "Room");
        sortSelector.getSelectionModel().selectFirst();
        sortSelector.setOnAction(e -> {
            if (controller != null) {
                controller.sortPendingReservations(sortSelector.getValue());
            }
        });
        sortSelector.setMaxWidth(Double.MAX_VALUE);

        Button verifyCodeBtn = new Button("Verify Code");
        verifyCodeBtn.setMaxWidth(Double.MAX_VALUE);
        verifyCodeBtn.setOnAction(e -> DiscountCodeVerificationView.display());

        Label discountLabel = new Label("Discounts :");
        discountLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label strategyLabel = new Label("Assignment strategy :");
        strategyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Label sortLabel = new Label("Sort by :");
        sortLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        VBox rightPanel = new VBox(15,
            styledControl(discountLabel, verifyCodeBtn),
            styledControl(strategyLabel, strategySelector),
            styledControl(sortLabel, sortSelector),
            reservationScroll
        );
        rightPanel.setPadding(new Insets(10));
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPrefWidth(300);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);


        HBox mainLayout = new HBox(20, leftPanel, rightPanel);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setPrefSize(winWidth, winHeight);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        updateDisplayedFloor(hotel.getFloors().get(0).getLetter());

        scene = new Scene(mainLayout, winWidth, winHeight);
        stage.setScene(scene);
        stage.setTitle("HELBHotel");
        stage.show();
    }

    private VBox styledControl(Label label, Control control) {
        VBox box = new VBox(3, label, control);
        box.setAlignment(Pos.CENTER_LEFT);
        control.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(control, Priority.NEVER);
        VBox.setVgrow(box, Priority.NEVER);
        return box;
    }    
    
    private VBox buildLegend() {
        HBox colorBar = new HBox(10);
        colorBar.setAlignment(Pos.CENTER);
        colorBar.getChildren().addAll(
            createLegendItem("Luxe", "#dda0dd"),
            createLegendItem("Business", "#add8e6"),
            createLegendItem("Economy", "#ffa500")
        );

        VBox legendBox = new VBox(5, colorBar);
        legendBox.setAlignment(Pos.CENTER);
        return legendBox;
    }

    private HBox createLegendItem(String label, String colorHex) {
        Label colorBox = new Label("     ");
        colorBox.setStyle("-fx-background-color: " + colorHex + "; -fx-border-radius: 5; -fx-background-radius: 5;");
        Label text = new Label(label);

        HBox box = new HBox(8, colorBox, text);
        box.setPadding(new Insets(5));
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-radius: 10;");
        return box;
    }

    private void updateDisplayedFloor(String floorLetter) {
        floorLayout.getChildren().clear();
        roomButtonsMap.clear();
    
        Floor floor = hotel.getFloor(floorLetter);
        if (floor == null) return;
    
        String[][] config = hotel.getBaseLayout();
    
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
    
        int rows = config.length;
        int cols = config[0].length;
    
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String type = config[row][col];
    
                int roomNumber = row * cols + col + 1;
                String roomId = floorLetter + roomNumber + type;
    
                if (type.equals("Z")) { // si chambre vide on crée un bouton désactivé
                    Button emptySpace = new Button();
                    emptySpace.setDisable(true);
                    emptySpace.setStyle("-fx-background-color: #cccccc; -fx-background-radius: 10;");
                    emptySpace.setPrefSize(90, 50);
                    grid.add(emptySpace, col, row);
                    continue;
                }
    
                Room room = floor.getRooms().get(roomId);
                if (room == null) continue;
    
                Button roomButton = new Button(room.getId());
                roomButton.setPrefSize(90, 50);
                roomButton.setOnAction(e -> {
                    boolean cancelled = RoomView.displayInfo(room);
                    if (cancelled) {
                        hotel.freeRoom(room.getId());
                        removeReservationCard(room.getId());
                    }
                });
    
                String color = room.isReserved() ? "#ff0000" : getColor(room.getType());
                roomButton.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");
                roomButtonsMap.put(room.getId(), roomButton);
                grid.add(roomButton, col, row);
            }
        }
    
        floorLayout.getChildren().add(grid);
    }    

    private String getColor(RoomType type) {
        if (type == RoomType.ECONOMIC) return "#ffa500";
        if (type == RoomType.BUSINESS) return "#add8e6";
        if (type == RoomType.LUXURY) return "#dda0dd";
        return "#ffffff";
    }

    private void updateRoomColor(String roomId) {
        Room room = hotel.getRoom(roomId);
        Button roomButton = roomButtonsMap.get(roomId);
        if (room != null && roomButton != null) {
            String color = room.isReserved() ? "#ff0000" : getColor(room.getType());
            roomButton.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");
        }
    }

    private void addReservationCard(Room room) {
        Reservation r = room.getReservation();
        if (r == null) return;

        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: " + getColor(room.getType()) + "; -fx-background-radius: 10; -fx-border-color: #ccc; -fx-border-radius: 10;");
        card.setUserData(room.getId());

        String name = r.getFirstName().charAt(0) + ". " + r.getLastName();
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label roomLabel = new Label(room.getId());

        card.getChildren().addAll(nameLabel, roomLabel);
        reservationList.getChildren().add(card);
    }

    private void removeReservationCard(String roomId) {
        reservationList.getChildren().removeIf(node -> roomId.equals(node.getUserData()));
    }
}
