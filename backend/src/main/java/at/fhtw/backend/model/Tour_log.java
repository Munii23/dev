package at.fhtw.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour_log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tour_log_id;
    @DateTimeFormat(pattern = "yyyy.MM.dd")

    @Column(nullable = false, name = "date")
    private Date date;
    @Column(nullable = true, name = "time")
    private Time time;
    @Column(length = 1000, nullable = true, name = "comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "difficulty")
    private Difficulty difficulty;

    @Column(nullable = false, name = "distance")
    private double distance;
    @Column(nullable = false, name = "total_time")
    private double total_time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "rating")
    private Rating rating;

    public Long getId() {
        return tour_log_id;
    }

    public Tour_log(Date date, Time time, String comment, Difficulty difficulty, double distance, double total_time, Rating rating) {
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.difficulty = difficulty;
        this.distance = distance;
        this.total_time = total_time;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "TourLog{" +
                "tour_log_id=" + tour_log_id +
                ", date=" + date +
                ", time=" + time +
                ", comment='" + comment + '\'' +
                ", difficulty=" + difficulty +
                ", distance=" + distance +
                ", total_time=" + total_time +
                ", rating=" + rating +
                '}';
    }

    public void setId(long l) {
        this.tour_log_id = l;
    }


    // getters and setters
    /// annotation für Local Date

}