import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerLogin extends GridPane {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button signInButton;
    private Button signUpButton;

    private List<String[]> users;

    public CustomerLogin() {
        // Initialize UI elements
        Label welcomeLabel = new Label("Welcome to CINEBOOK!");
        welcomeLabel.setFont(new Font("Arial", 20));

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        signInButton = new Button("Sign In");
        signUpButton = new Button("Sign Up");

        // Load users from the file
        users = loadUsersFromFile("JAVA//users.txt");

        // Set the constraints for the form
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        // Add the form to the grid
        add(welcomeLabel, 0, 0, 2, 1);
        add(usernameLabel, 0, 1);
        add(usernameField, 1, 1);
        add(passwordLabel, 0, 2);
        add(passwordField, 1, 2);
        add(signInButton, 0, 3);
        add(signUpButton, 1, 3);

        // Set the grid's constraints
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));
        setStyle("-fx-background-color: #AE445A;");

        // Set the action for the sign up button
    signUpButton.setOnAction(event -> {
        SignUpScreen signUpScreen = new SignUpScreen();
        Stage signUpStage = new Stage();
        signUpStage.setTitle("SignUpScreen");
        signUpStage.setScene(new Scene(signUpScreen, 500, 500));
        signUpStage.show();
    });

        // Set the action for the sign in button
        signInButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Check if the username and password are valid
            if (isValidCredentials(username, password)) {
                // Open the customer screen
                CustomerLogin customerScreen = new CustomerLogin();
                StackPane customerScreenPane = new StackPane(customerScreen);
                customerScreenPane.setPadding(new Insets(10));
                Scene customerScene = new Scene(customerScreenPane, 500, 500);
                Stage customerStage = new Stage();
                customerStage.setTitle("Customer Screen");
                customerStage.setScene(customerScene);
                customerStage.show();
            } else {
                // Show an error message if the credentials are invalid
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid credentials");
                errorAlert.setContentText("Please enter a valid username and password.");
                errorAlert.showAndWait();
            }
        });

        // Set the action for the sign up button
        signInButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
        
            // Check if the username and password are valid
            if (isValidCredentials(username, password)) {
        
                // Open the customer screen
                Customer customerScreen = new Customer();
        
        
                
                
            } else {
                // Show an error message if the credentials are invalid
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Invalid credentials");
                errorAlert.setContentText("Please enter a valid username and password.");
                errorAlert.showAndWait();
            }
        });}

    private List<String[]> loadUsersFromFile(String filePath) {
    Map<String, String[]> usersMap = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        String[] userData;
        while ((line = br.readLine()) != null) {
            userData = line.split(",");
            if (userData.length == 3) {
                String username = userData[0];
                String[] existingUserData = usersMap.get(username);
                if (existingUserData == null) {
                    usersMap.put(username, userData);
                } else {
                    String[] newUserData = new String[existingUserData.length + userData.length];
                    System.arraycopy(existingUserData, 0, newUserData, 0, existingUserData.length);
                    System.arraycopy(userData, 0, newUserData, existingUserData.length, userData.length);
                    usersMap.put(username, newUserData);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    List<String[]> users = new ArrayList<>(usersMap.values());
    return users;
}
    private boolean isValidCredentials(String username, String password) {
    return users.stream()
            .anyMatch(user -> user[0].equals(username) && user[user.length - 1].equals(password));
}
}

