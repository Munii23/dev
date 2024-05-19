package at.fhtw.backend.controller;

import at.fhtw.backend.model.Tour_log;
import at.fhtw.backend.repository.TourLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tour_log")
public class TourLogController {

    @Autowired
    private TourLogRepository tourLogRepository;

    @GetMapping
    public List<Tour_log> getAllTourLog() {
        return tourLogRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Tour_log> createTourLog(@RequestBody Tour_log tourLog) {
        System.out.println("Received TourLog: " + tourLog.toString());
        Tour_log savedTourLog = tourLogRepository.save(tourLog);
        return ResponseEntity.ok(savedTourLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tour_log> updateTourLog(@PathVariable Long id, @RequestBody Tour_log tourLogDetails) {
        Optional<Tour_log> optionalTourLog = tourLogRepository.findById(id);
        if (optionalTourLog.isPresent()) {
            Tour_log tourLog = optionalTourLog.get();
            tourLog.setDate(tourLogDetails.getDate());
            tourLog.setTime(tourLogDetails.getTime());
            tourLog.setComment(tourLogDetails.getComment());
            tourLog.setDifficulty(tourLogDetails.getDifficulty());
            tourLog.setDistance(tourLogDetails.getDistance());
            tourLog.setTotal_time(tourLogDetails.getTotal_time());
            tourLog.setRating(tourLogDetails.getRating());
            Tour_log updatedTourLog = tourLogRepository.save(tourLog);
            return ResponseEntity.ok(updatedTourLog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourLog(@PathVariable Long id) {
        System.out.println("Deleted TourLog with id: " + id);
        tourLogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
