package at.fhtw.tourplanner.model;

import java.util.List;

public class Tour {
    private Long tourId;
    private String name;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String transportType;
    private double distance;
    private double estimatedTime;
    private String routeInformation;
    private List<Double> fromCoordinates;
    private List<Double> toCoordinates;
    private int popularity;
    private int childFriendliness;

    // Standardkonstruktor (No-Argument-Konstruktor)
    public Tour() {
    }

    // Konstruktor mit allen Feldern
    public Tour(Long tourId, String name, String description, String fromLocation, String toLocation,
                String transportType, double distance, double estimatedTime, String routeInformation,
                List<Double> fromCoordinates, List<Double> toCoordinates, int popularity, int childFriendliness) {
        this.tourId = tourId;
        this.name = name;
        this.description = description;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.transportType = transportType;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.routeInformation = routeInformation;
        this.fromCoordinates = fromCoordinates;
        this.toCoordinates = toCoordinates;
        this.popularity = popularity;
        this.childFriendliness = childFriendliness;
    }

    // Getter und Setter für alle Felder
    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getRouteInformation() {
        return routeInformation;
    }

    public void setRouteInformation(String routeInformation) {
        this.routeInformation = routeInformation;
    }

    public List<Double> getFromCoordinates() {
        return fromCoordinates;
    }

    public void setFromCoordinates(List<Double> fromCoordinates) {
        this.fromCoordinates = fromCoordinates;
    }

    public List<Double> getToCoordinates() {
        return toCoordinates;
    }

    public void setToCoordinates(List<Double> toCoordinates) {
        this.toCoordinates = toCoordinates;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getChildFriendliness() {
        return childFriendliness;
    }

    public void setChildFriendliness(int childFriendliness) {
        this.childFriendliness = childFriendliness;
    }
}
