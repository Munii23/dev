package at.fhtw.tourplanner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tour_log {
    @JsonProperty("tour_log_id")
    private int tourLogId;
    @JsonProperty("date")
    private String date;
    @JsonProperty("time")
    private String time;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("difficulty")
    private Difficulty difficulty;  // Ändern zu Difficulty
    @JsonProperty("distance")
    private double distance;
    @JsonProperty("total_time")
    private double totalTime;
    @JsonProperty("rating")
    private Rating rating;  // Ändern zu Rating
    @JsonProperty("id")
    private int id;

    public Tour_log() {
    }

    public Tour_log(int tourLogId, String date, String time, String comment, Difficulty difficulty, double distance, double totalTime, Rating rating, int id) {
        this.tourLogId = tourLogId;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.difficulty = difficulty;
        this.distance = distance;
        this.totalTime = totalTime;
        this.rating = rating;
        this.id = id;
    }

    public Tour_log(String date, String time, String comment, Difficulty difficulty, double distance, double totalTime, Rating rating) {
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.difficulty = difficulty;
        this.distance = distance;
        this.totalTime = totalTime;
        this.rating = rating;
    }
}
