package at.fhtw.backend.repository;

import at.fhtw.backend.model.Tour_log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourLogRepository extends JpaRepository<Tour_log, Long> {

}
