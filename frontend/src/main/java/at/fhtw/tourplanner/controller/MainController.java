package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.Main;
import at.fhtw.tourplanner.http.RestServiceClient;
import at.fhtw.tourplanner.model.TableViewHelper;
import at.fhtw.tourplanner.model.Tour;
import at.fhtw.tourplanner.model.Tour_log;
import at.fhtw.tourplanner.util.AutocompleteHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public Label labelTours;
    @FXML
    public Button addTourButton;
    @FXML
    public Button removeTourButton;
    @FXML
    private Button refreshLogButton;
    @FXML
    public Button moreTourOptionsButton;
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab tabGeneral;
    @FXML
    public AnchorPane anchorPaneGeneral;
    @FXML
    public Tab tabRoute;
    @FXML
    public AnchorPane anchorPaneRoute;
    @FXML
    public ImageView imageViewRoute;
    @FXML
    public Tab tabMisc;
    @FXML
    public AnchorPane anchorPaneMisc;
    @FXML
    public Label labelTourLogs;
    @FXML
    public Button addLogButton;
    @FXML
    public Button removeLogButton;
    @FXML
    public Button moreLogOptionsButton;
    @FXML
    public Menu menuFile;
    @FXML
    public Menu menuEdit;
    @FXML
    public Menu menuOptions;
    @FXML
    public Menu menuSpacer;
    @FXML
    public Menu menuSpacer2;
    @FXML
    public Menu menuHelp;
    @FXML
    public TextField searchBar;

    @FXML
    private ListView<String> searchResults;
    @FXML
    public ListView<Tour> tourList;
    @FXML
    private TableView<Tour_log> tourLogsTable;
    @FXML
    private TableColumn<?, ?> dateColumn;
    @FXML
    private TableColumn<?, ?> durationColumn;
    @FXML
    private TableColumn<?, ?> distanceColumn;
    @FXML
    private Button importToursButton;
    @FXML
    private Button exportToursButton;
    private ObservableList<List<Double>> savedItems = FXCollections.observableArrayList();
    @FXML
    private Button calculateRoute;
    @FXML
    private ObservableList<Tour_log> logData = FXCollections.observableArrayList();
    private ObservableList<Tour> tourData = FXCollections.observableArrayList();
    private List<Map<String, Object>> completeSuggestions = new LinkedList<>();

    @Getter
    private String result;
     @Getter
     @FXML
    private WebView webView;
   @Getter
   @FXML
    private WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logData = FXCollections.observableArrayList();
        TableViewHelper.stretchTableColumnsToFill(tourLogsTable, dateColumn, durationColumn, distanceColumn);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        initializeHandlers(); // Event-Handler setzen

        webEngine = webView.getEngine();
        tourLogsTable.setItems(logData);
        tourList.setItems(tourData);
        loadTours();
        loadData();

        // Set Cell Factory to display tour name
        tourList.setCellFactory(new Callback<ListView<Tour>, ListCell<Tour>>() {
            @Override
            public ListCell<Tour> call(ListView<Tour> param) {
                return new ListCell<Tour>() {
                    @Override
                    protected void updateItem(Tour tour, boolean empty) {
                        super.updateItem(tour, empty);
                        if (empty || tour == null) {
                            setText(null);
                        } else {
                            setText(tour.getName());
                        }
                    }
                };
            }
        });

        addLogButton.setOnAction(event -> openPopupTourLog());
        removeLogButton.setOnAction(event -> deleteLogEntry());
        refreshLogButton.setOnAction(event -> loadData());
        addTourButton.setOnAction(event -> handleAddTourButtonAction());
        removeTourButton.setOnAction(event -> handleRemoveTourButtonAction());
    }

    private void addLogEntry() {
        try {
            RestServiceClient client = new RestServiceClient();
            String json = client.fetchData("/tour_log");

            ObjectMapper mapper = new ObjectMapper();
            List<Tour_log> entries = mapper.readValue(json, new TypeReference<List<Tour_log>>() {});

            logData.addAll(entries);
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPopupTourLog() {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/at/fhtw/tourplanner/PopUpTourLog.fxml"));
            Parent root = fxmlloader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Tour Log");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening the log entry window: " + e.getMessage());
        }
    }

    private void deleteLogEntry() {
        Tour_log selectedLog = tourLogsTable.getSelectionModel().getSelectedItem();
        if (selectedLog != null) {
            logData.remove(selectedLog);
            deleteTourLogFromServer(selectedLog);
        }
    }

    private void deleteTourLogFromServer(Tour_log tourLog) {
        try {
            RestServiceClient client = new RestServiceClient();
            client.deleteData("/tour_log/" + tourLog.getId());
        } catch (Exception e) {
            System.err.println("Error deleting tour log from server: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            RestServiceClient client = new RestServiceClient();
            String json = client.fetchData("/tour_log");

            ObjectMapper mapper = new ObjectMapper();
            List<Tour_log> entries = mapper.readValue(json, new TypeReference<List<Tour_log>>() {});
            logData.clear();
            logData.addAll(entries);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading tour logs: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchBarKeyReleased(KeyEvent keyEvent) {
        String searchText = searchBar.getText().trim();
        System.out.println("Key Released Event Triggered with text: " + searchText);

        if (searchText.length() >= 3) {
            fetchSuggestions(searchText);
        } else {
            searchResults.setItems(FXCollections.observableArrayList());
        }
    }

    private void fetchSuggestions(String searchText) {
        try {
            RestServiceClient client = new RestServiceClient();
            completeSuggestions = client.getAutocompleteSuggestions("/autocomplete", searchText);
            List<String> suggestions = new AutocompleteHelper().getLabelFromSuggestions(completeSuggestions);
            searchResults.setItems(FXCollections.observableArrayList(suggestions));
            System.out.println("Suggestions fetched: " + suggestions);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error getting autocomplete suggestions: " + e.getMessage());
        }
    }

    private void initializeHandlers() {
        searchBar.setOnKeyReleased(this::handleSearchBarKeyReleased);

        searchResults.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = searchResults.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    Map<String, Object> selectedSuggestion = completeSuggestions.stream()
                            .filter(suggestion -> selectedItem.equals(suggestion.get("label")))
                            .findFirst()
                            .orElse(null);
                    if (selectedSuggestion != null) {
                        List<Double> coordinates = new AutocompleteHelper().getCoordinatesFromSuggestion(selectedSuggestion);
                        if (coordinates != null && !savedItems.contains(coordinates)) {
                            savedItems.add(coordinates);
                            System.out.println("Saved Items: " + savedItems);
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void calculateRoute() {
        if (savedItems.size() < 2) {
            System.err.println("Not enough coordinates to calculate route");
            return;
        }

        List<Double> endCoordinates = savedItems.get(savedItems.size() - 1);
        List<Double> startCoordinates = savedItems.get(savedItems.size() - 2);
        String result = "";

        try {
            RestServiceClient client = new RestServiceClient();
            result = client.postRoute(startCoordinates, endCoordinates);
            saveDirectionsToFile(result);
            showMapWithRoute(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error calculating route: " + e.getMessage());
        }
        System.out.println("Route calculated: " + result);
    }

    public void showBrowser() {
        Main.showMapInDefaultBrowser();
    }

    public void calculateRouteAndShowBrowser() {
        calculateRoute();
        showBrowser();
    }

    public void saveDirectionsToFile(String directions) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object json = mapper.readValue(directions, Object.class);
            String indented = mapper.writeValueAsString(json);

            try (FileWriter fileWriter = new FileWriter("src/main/resources/directions.js")) {
                fileWriter.write("var directions = " + indented + ";");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTourButtonAction() {
        openPopupTour();
    }

    @FXML
    private void handleRemoveTourButtonAction() {
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            deleteTour(selectedTour.getTourId());
        }
    }

    private void openPopupTour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/fhtw/tourplanner/PopUpTour.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Tour");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening the tour popup window: " + e.getMessage());
        }
    }

    private void deleteTour(Long tourId) {
        try {
            RestServiceClient client = new RestServiceClient();
            client.deleteData("/tours/" + tourId);
            loadTours();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error deleting tour: " + e.getMessage());
        }
    }

    public void loadTours() {
        try {
            RestServiceClient client = new RestServiceClient();
            List<Tour> tours = client.fetchTours("/tours");
            tourData.clear();
            tourData.addAll(tours);
            tourList.setItems(tourData);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading tours: " + e.getMessage());
        }
    }

    public void showMapWithRoute(String directions) {
        webEngine.load(getClass().getResource("/leaflet.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                webEngine.executeScript("var directions = " + directions + ";");
            }
        });
    }

    @FXML
    private void handleImportToursButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        Stage stage = (Stage) importToursButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            importTours(selectedFile);
        }
    }

    @FXML
    private void handleExportToursButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("tours.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        Stage stage = (Stage) exportToursButton.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            exportTours(selectedFile);
        }
    }

    private void importTours(File file) {
        try {
            RestServiceClient client = new RestServiceClient();
            client.uploadFile("/tours/import", file);
            loadTours();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error importing tours: " + e.getMessage());
        }
    }

    private void exportTours(File file) {
        try {
            RestServiceClient client = new RestServiceClient();
            String csvContent = client.downloadFile("/tours/export");
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(csvContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error exporting tours: " + e.getMessage());
        }
    }
}
