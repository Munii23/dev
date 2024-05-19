package at.fhtw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tourId;
    private String name;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String transportType;
    private double distance;
    private double estimatedTime;
    private String routeInformation;

    @ElementCollection
    private List<Double> fromCoordinates;

    @ElementCollection
    private List<Double> toCoordinates;

    private int popularity;
    private int childFriendliness;

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
}
