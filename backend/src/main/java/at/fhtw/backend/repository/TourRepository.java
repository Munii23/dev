package at.fhtw.backend.repository;

import at.fhtw.backend.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TourRepository extends JpaRepository<Tour, Long> {
}
