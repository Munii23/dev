package at.fhtw.tourplanner;

import at.fhtw.tourplanner.controller.MainController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    private static HostServices hostServices;
    private static MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        showStage(primaryStage);
        hostServices = getHostServices();
    }

    public static Parent showStage(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("mainWindow.fxml")));
        Parent root = loader.load();

        // Set the mainController
        mainController = loader.getController();

        Scene scene = new Scene(root, 400, 275);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/at/fhtw/tourplanner/styles.css")).toExternalForm());
        primaryStage.setTitle("Tour Planner");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(800);
        primaryStage.show();
        return root;
    }

    public static void showMapInDefaultBrowser(){
        hostServices.showDocument(Main.class.getResource("/leaflet.html").toExternalForm());
    }

    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
