// Datei: backend/src/main/java/at/fhtw/backend/controller/TourController.java
package at.fhtw.backend.controller;

import at.fhtw.backend.model.Tour;
import at.fhtw.backend.service.TourService;
import at.fhtw.backend.util.CsvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public List<Tour> getAllTours() {
        return tourService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable Long id) {
        Optional<Tour> tour = tourService.findById(id);
        return tour.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tour> createTour(@RequestBody Tour tour) {
        Tour savedTour = tourService.save(tour);
        return ResponseEntity.ok(savedTour);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateTour(@PathVariable Long id, @RequestBody Tour tourDetails) {
        Optional<Tour> optionalTour = tourService.findById(id);
        if (optionalTour.isPresent()) {
            Tour tour = optionalTour.get();
            tour.setName(tourDetails.getName());
            tour.setDescription(tourDetails.getDescription());
            tour.setFromLocation(tourDetails.getFromLocation());
            tour.setToLocation(tourDetails.getToLocation());
            tour.setTransportType(tourDetails.getTransportType());
            tour.setDistance(tourDetails.getDistance());
            tour.setEstimatedTime(tourDetails.getEstimatedTime());
            tour.setRouteInformation(tourDetails.getRouteInformation());
            Tour updatedTour = tourService.save(tour);
            return ResponseEntity.ok(updatedTour);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        tourService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportTours() {
        String filename = "tours.csv";
        InputStreamResource file = new InputStreamResource(tourService.exportTours());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importTours(@RequestParam("file") MultipartFile file) {
        tourService.importTours(file);
        return ResponseEntity.ok("File imported successfully.");
    }

    @GetMapping("/search")
    public List<Tour> searchTours(@RequestParam String query) {
        return tourService.searchTours(query);
    }
}
