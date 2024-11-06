import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Customer {

    BorderPane borderPane;
    TableView<List<String>> tableView;
    ObservableList<List<String>> observableData;
    TextField field1, field2, field3, field4;

    public Customer() {
        // Create a table view
        tableView = new TableView<>();

        // Create table columns
        TableColumn<List<String>, String> col1 = new TableColumn<>("Title");
        TableColumn<List<String>, String> col2 = new TableColumn<>("Duration (minutes)");
        TableColumn<List<String>, String> col3 = new TableColumn<>("Price (Birr)");
        TableColumn<List<String>, String> col4 = new TableColumn<>("ShowTime");

        // Set cell value factories for the columns
        col1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        col2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        col3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        col4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));

        // Add the columns to the table view
        tableView.getColumns().addAll(col1, col2, col3, col4);

        // Read data from a CSV file
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("JAVA//movies.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 4) {
                    List<String> row = new ArrayList<>();
                    for (String value : values) {
                        row.add(value);
                    }
                    data.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the items of the table view
        observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);

        Button selectSeatButton = new Button("Select Seat");
        selectSeatButton.setOnAction(event -> {
            List<String> selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("No Movie Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a movie from the table.");
                alert.showAndWait();
            } else {
                String selectedMovie = selectedItem.get(0);
                double selectedMoviePrice = Double.parseDouble(selectedItem.get(2));
                Seat seat = new Seat(selectedMovie, selectedMoviePrice);
                System.out.println("Selected Movie: " + selectedMovie);
            }
        });

HBox buttonBox = new HBox(selectSeatButton);
buttonBox.setPadding(new Insets(10));
        
        borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        borderPane.setBottom(buttonBox);
        Scene scene = new Scene(borderPane, 600, 400);

        // Set the stage title and scene
        Stage primaryStage = new Stage();
        primaryStage.setTitle("MOVIES");
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
        Image icon= new Image("Logo.jpeg");
        primaryStage.getIcons().add(icon);
    }
    
   

    public static void main(String[] args) {
        Application.launch(args);
    }
}
