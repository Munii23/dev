package at.fhtw.backend.service;

import at.fhtw.backend.model.Tour_log;
import at.fhtw.backend.repository.TourLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourLogService {

    private final TourLogRepository tourLogRepository;

    @Autowired
    public TourLogService(TourLogRepository tourLogRepository) {
        this.tourLogRepository = tourLogRepository;
    }

    public List<Tour_log> findAll() {
        return tourLogRepository.findAll();
    }

    public Optional<Tour_log> findById(Long id) {
        return tourLogRepository.findById(id);
    }

    public Tour_log save(Tour_log tourLog) {
        return tourLogRepository.save(tourLog);
    }

    public void deleteById(Long id) {
        tourLogRepository.deleteById(id);
    }
}
