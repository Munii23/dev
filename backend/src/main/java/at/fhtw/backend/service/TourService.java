package at.fhtw.backend.service;

import at.fhtw.backend.model.Tour;
import at.fhtw.backend.repository.TourRepository;
import at.fhtw.backend.util.CsvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    public Optional<Tour> findById(Long id) {
        return tourRepository.findById(id);
    }

    public Tour save(Tour tour) {
        return tourRepository.save(tour);
    }

    public void deleteById(Long id) {
        tourRepository.deleteById(id);
    }

    public void saveAll(List<Tour> tours) {
        tourRepository.saveAll(tours);
    }

    public ByteArrayInputStream exportTours() {
        List<Tour> tours = findAll();
        return CsvHelper.toursToCsv(tours);
    }

    public void importTours(MultipartFile file) {
        List<Tour> tours = CsvHelper.csvToTours(file);
        saveAll(tours);
    }

    public List<Tour> searchTours(String query) {
        List<Tour> allTours = findAll();
        return allTours.stream()
                .filter(tour -> tour.getName().contains(query) ||
                        tour.getDescription().contains(query) ||
                        tour.getFromLocation().contains(query) ||
                        tour.getToLocation().contains(query) ||
                        String.valueOf(tour.getDistance()).contains(query) ||
                        String.valueOf(tour.getEstimatedTime()).contains(query) ||
                        String.valueOf(tour.getPopularity()).contains(query) ||
                        String.valueOf(tour.getChildFriendliness()).contains(query))
                .collect(Collectors.toList());
    }

}
