module tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires static lombok;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires jdk.jsobject;
    requires javafx.web;

    opens at.fhtw.tourplanner to javafx.graphics, javafx.fxml;
    exports at.fhtw.tourplanner;
    exports at.fhtw.tourplanner.model;
    exports at.fhtw.tourplanner.controller;
    opens at.fhtw.tourplanner.controller to javafx.fxml, javafx.graphics;
}
