package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.http.RestServiceClient;
import at.fhtw.tourplanner.model.Difficulty;
import at.fhtw.tourplanner.model.Rating;
import at.fhtw.tourplanner.model.Tour_log;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopUpTourLogController {

    @FXML
    private TextField dateField;
    @FXML
    private TextField timeField;
    @FXML
    private TextField commentField;
    @FXML
    private ComboBox<Difficulty> difficultyField;
    @FXML
    private TextField distanceField;
    @FXML
    private TextField totalTimeField;
    @FXML
    private ComboBox<Rating> ratingField;

    @FXML
    public void initialize() {
        ratingField.getItems().setAll(Rating.values());
        difficultyField.getItems().setAll(Difficulty.values());
    }

    @FXML
    private void handleSubmitButtonAction() {
        try {
            if (dateField.getText().isEmpty() || timeField.getText().isEmpty() || commentField.getText().isEmpty() ||
                    difficultyField.getValue() == null || distanceField.getText().isEmpty() || totalTimeField.getText().isEmpty() ||
                    ratingField.getValue() == null) {
                System.err.println("All fields must be filled");
                return;
            }

            String date = dateField.getText();
            String time = timeField.getText();
            String comment = commentField.getText();
            Difficulty difficulty = difficultyField.getValue();
            double distance = Double.parseDouble(distanceField.getText());
            double totalTime = Double.parseDouble(totalTimeField.getText());
            Rating rating = ratingField.getValue();

            Tour_log tourLog = new Tour_log(date, time, comment, difficulty, distance, totalTime, rating);

            sendTourLogToServer(tourLog);

            Stage stage = (Stage) dateField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            System.err.println("Error processing the inputs: " + e.getMessage());
        }
    }

    private void sendTourLogToServer(Tour_log tourLog) {
        try {
            RestServiceClient client = new RestServiceClient();
            client.sendData("/tour_log", tourLog);
        } catch (Exception e) {
            System.err.println("Error sending tour log to server: " + e.getMessage());
        }
    }
}
