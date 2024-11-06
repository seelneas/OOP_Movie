import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Seat {
    private final String csvFilePath = "JAVA//seat_selections.csv"; // CSV file path
    private static Label selectedSeatsLabel;
    private static Label totalPriceLabel;
    private int selectedSeats = 0;
    private final int numRows = 5;
    private final int numSeatsPerRow = 10;
    private Button[][] seatButtons = new Button[numRows][numSeatsPerRow];
    private boolean[][] seatSelected = new boolean[numRows][numSeatsPerRow];
    private Map<String, boolean[][]> seatSelectedMap = new HashMap<>();
    private final double normalSeatPrice;
    private final double vipSeatPriceMultiplier;
    private double totalPrice;

    
    public Seat(String selectedMovie, double selectedMoviePrice) {
        normalSeatPrice = selectedMoviePrice;
        vipSeatPriceMultiplier = 2 * selectedMoviePrice;

        GridPane root = new GridPane();
        root.setHgap(5);
        root.setVgap(5);
        root.setStyle("-fx-background-color: #384358;");


        selectedSeatsLabel = new Label("Selected Seats: 0");
        selectedSeatsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: white;");
        root.add(selectedSeatsLabel, 0, 0, 2, 1);

        // Set VIP label on top
        Label vipLabel = new Label("VIP(Double Price)");
        vipLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: white;");
        root.add(vipLabel, 0, 1, 2, 1);

        char rowLabel = 'A';
        for (int row = 0; row < numRows; row++) {
            for (int seat = 0; seat < numSeatsPerRow; seat++) {
                final int currentRow = row;
                final int currentSeat= seat;
                seatButtons[row][seat] = new Button(rowLabel + String.valueOf(seat + 1));

                // Set golden color for VIP seats
                if (seat < 2) {
                    seatButtons[row][seat].setStyle("-fx-background-color: gold;");
                } else {
                    seatButtons[row][seat].setStyle("-fx-background-color: white;");
                }

                seatButtons[row][seat].setOnAction(e -> handleSeatSelection(selectedMovie, currentRow, currentSeat));
                root.add(seatButtons[row][seat], seat, row + 2);
            }
            rowLabel++;
        }
        
        Button finishButton = new Button("CONFIRM PURCHASE");
finishButton.setOnAction(e ->handleFinishButton(selectedMovie));
root.add(finishButton, 0, numRows + 3, numSeatsPerRow, 1);

        totalPriceLabel= new Label("Total Price: 0.00");
        HBox priceBox= new HBox(totalPriceLabel);
        root.add(priceBox, 0, numRows + 2, numSeatsPerRow, 1);

        displaySeats(selectedMovie);

        
        Stage primaryStage= new Stage();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        Image icon= new Image("Logo.jpeg");
        primaryStage.getIcons().add(icon);
    }

    // ...

    private void displaySeats(String selectedMovie) {
        boolean[][] seatSelected = seatSelectedMap.get(selectedMovie);
    
        if (seatSelected != null) {
            for (int row = 0; row < numRows; row++) {
                for (int seat = 0; seat < numSeatsPerRow; seat++) {
                    if (seatSelected[row][seat]) {
                        seatButtons[row][seat].setDisable(true);
                    } else {
                        seatButtons[row][seat].setDisable(false);
                    }
                }
            }
        } else {
            seatSelected = new boolean[numRows][numSeatsPerRow];
            seatSelectedMap.put(selectedMovie, seatSelected);
        }
    
        // Check if there is seat selection information from the file
        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] seatInfo = line.split(",");
                if (seatInfo.length == 3) {
                    int row = Integer.parseInt(seatInfo[0]);
                    int seat = Integer.parseInt(seatInfo[1]);
                    String movie = seatInfo[2];
                    if (movie.equals(selectedMovie)) {
                        seatButtons[row][seat].setDisable(true);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


private void handleSeatSelection(String selectedMovie, int row, int seat) {
    boolean[][] seatSelected = seatSelectedMap.get(selectedMovie);
    Button seatButton = seatButtons[row][seat];

    if (!seatSelected[row][seat]) {
        seatSelected[row][seat] = true;
        seatButton.setStyle("-fx-background-color: green;"); // Set selected seat color to green
        selectedSeats++;
        selectedSeatsLabel.setText("Selected Seats: " + selectedSeats);
    } else {
        seatSelected[row][seat] = false;
        seatButton.setStyle("-fx-background-color: white;"); // Set unselected seat color to white
        selectedSeats--;
        selectedSeatsLabel.setText("Selected Seats: " + selectedSeats);
    }

    totalPrice = calculateTotalPrice(selectedMovie);
    totalPriceLabel.setText(String.format("Total Price: %.2f", totalPrice));
    totalPriceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: white;");

    
}



private void handleFinishButton(String selectedMovie) {
    // Disable only the selected seats
    boolean[][] seatSelected = seatSelectedMap.get(selectedMovie);
    for (int row = 0; row < numRows; row++) {
        for (int seat = 0; seat < numSeatsPerRow; seat++) {
            if (seatSelected[row][seat]) {
                seatButtons[row][seat].setDisable(true);
            }
        }
    }

    // Save seat selections to CSV file
    saveSeatSelections(selectedMovie);
    Confirmation cPage= new Confirmation(selectedMovie, selectedSeats, totalPrice);
}


private void saveSeatSelections(String selectedMovie) {
    try (FileWriter writer = new FileWriter(csvFilePath, true)) {
        boolean[][] seatSelected = seatSelectedMap.get(selectedMovie);
        for (int row = 0; row < numRows; row++) {
            for (int seat = 0; seat < numSeatsPerRow; seat++) {
                if (seatSelected[row][seat]) {
                    String seatInfo = row + "," + seat + "," + selectedMovie + "\n";
                    writer.write(seatInfo);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private double calculateTotalPrice(String selectedMovie) {
    boolean[][] seatSelected = seatSelectedMap.get(selectedMovie);
    double totalPrice = 0.0;

    for (int row = 0; row < numRows; row++) {
        for (int seat = 0; seat < numSeatsPerRow; seat++) {
            if (seatSelected[row][seat]) {
                if (seat < 2) {
                    totalPrice += vipSeatPriceMultiplier;
                } else {
                    totalPrice += normalSeatPrice;
                }
            }
        }
    }

    return totalPrice;
}
    public static void main(String[] args) {
        Application.launch(args);
    }

   
}
