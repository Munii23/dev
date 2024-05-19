package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.Main;
import at.fhtw.tourplanner.http.RestServiceClient;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.util.AutocompleteHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class PopUpTourController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField fromLocationField;
    @FXML
    private ListView<String> fromLocationResults;
    @FXML
    private TextField toLocationField;
    @FXML
    private ListView<String> toLocationResults;
    @FXML
    private TextField transportTypeField;
    @FXML
    private TextField distanceField;
    @FXML
    private TextField estimatedTimeField;
    @FXML
    private TextField routeInformationField;

    private List<Map<String, Object>> fromLocationSuggestions;
    private List<Map<String, Object>> toLocationSuggestions;

    private List<Double> fromCoordinates;
    private List<Double> toCoordinates;

    @FXML
    private void handleFromLocationKeyReleased(KeyEvent keyEvent) {
        String searchText = fromLocationField.getText().trim();
        if (searchText.length() >= 3) {
            fetchFromLocationSuggestions(searchText);
        } else {
            fromLocationResults.setItems(FXCollections.observableArrayList());
        }
    }

    private void fetchFromLocationSuggestions(String searchText) {
        try {
            RestServiceClient client = new RestServiceClient();
            fromLocationSuggestions = client.getAutocompleteSuggestions("/autocomplete", searchText);
            List<String> suggestions = new AutocompleteHelper().getLabelFromSuggestions(fromLocationSuggestions);
            fromLocationResults.setItems(FXCollections.observableArrayList(suggestions));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleToLocationKeyReleased(KeyEvent keyEvent) {
        String searchText = toLocationField.getText().trim();
        if (searchText.length() >= 3) {
            fetchToLocationSuggestions(searchText);
        } else {
            toLocationResults.setItems(FXCollections.observableArrayList());
        }
    }

    private void fetchToLocationSuggestions(String searchText) {
        try {
            RestServiceClient client = new RestServiceClient();
            toLocationSuggestions = client.getAutocompleteSuggestions("/autocomplete", searchText);
            List<String> suggestions = new AutocompleteHelper().getLabelFromSuggestions(toLocationSuggestions);
            toLocationResults.setItems(FXCollections.observableArrayList(suggestions));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        fromLocationResults.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = fromLocationResults.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    fromLocationField.setText(selectedItem);
                    fromCoordinates = getCoordinatesFromSelectedItem(fromLocationSuggestions, selectedItem);
                    fromLocationResults.setItems(FXCollections.observableArrayList());
                    System.out.println("From Coordinates: " + fromCoordinates);
                }
            }
        });

        toLocationResults.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = toLocationResults.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    toLocationField.setText(selectedItem);
                    toCoordinates = getCoordinatesFromSelectedItem(toLocationSuggestions, selectedItem);
                    toLocationResults.setItems(FXCollections.observableArrayList());
                    System.out.println("To Coordinates: " + toCoordinates);
                }
            }
        });
    }

    private List<Double> getCoordinatesFromSelectedItem(List<Map<String, Object>> suggestions, String selectedItem) {
        for (Map<String, Object> suggestion : suggestions) {
            if (selectedItem.equals(suggestion.get("label"))) {
                return new AutocompleteHelper().getCoordinatesFromSuggestion(suggestion);
            }
        }
        return null;
    }

    @FXML
    private void handleSubmitButtonAction() {
        try {
            // Eingabewerte aus den Feldern abrufen
            String name = nameField.getText();
            String description = descriptionField.getText();
            String fromLocation = fromLocationField.getText();
            String toLocation = toLocationField.getText();
            String transportType = transportTypeField.getText();
            double distance = Double.parseDouble(distanceField.getText());
            double estimatedTime = Double.parseDouble(estimatedTimeField.getText());
            String routeInformation = routeInformationField.getText();

            // Neues Tour-Objekt erstellen
            Tour tour = new Tour(null, name, description, fromLocation, toLocation, transportType, distance, estimatedTime, routeInformation, fromCoordinates, toCoordinates, 0, 0);

            // RestClient erstellen und Tourdaten an den Server senden
            RestServiceClient client = new RestServiceClient();
            client.sendData("/tours", tour);

            System.out.println("Tour sent to server: " + tour);

            // Pop-Up-Fenster schließen
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

            // Tourenliste aktualisieren
            MainController mainController = Main.getMainController();
            mainController.loadTours();

        } catch (NumberFormatException e) {
            System.err.println("Error processing the inputs: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error sending tour to server: " + e.getMessage());
        }
    }

}
