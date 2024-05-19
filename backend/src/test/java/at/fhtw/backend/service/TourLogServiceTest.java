package at.fhtw.backend.service;

import at.fhtw.backend.model.Tour_log;
import at.fhtw.backend.model.Difficulty;
import at.fhtw.backend.model.Rating;
import at.fhtw.backend.repository.TourLogRepository;
import at.fhtw.backend.service.TourLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TourLogServiceTest {

    @InjectMocks
    private TourLogService tourLogService;

    @Mock
    private TourLogRepository tourLogRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Tour_log log1 = new Tour_log(LocalDate.now(), LocalTime.now(), "Test comment 1", Difficulty.EASY, 10.0, 60.0, Rating.GOOD);
        Tour_log log2 = new Tour_log(LocalDate.now(), LocalTime.now(), "Test comment 2", Difficulty.HARD, 20.0, 120.0, Rating.BAD);
        when(tourLogRepository.findAll()).thenReturn(Arrays.asList(log1, log2));

        List<Tour_log> logs = tourLogService.findAll();

        assertEquals(2, logs.size());
        verify(tourLogRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Tour_log log = new Tour_log(LocalDate.now(), LocalTime.now(), "Test comment", Difficulty.EASY, 10.0, 60.0, Rating.GOOD);
        when(tourLogRepository.findById(1L)).thenReturn(Optional.of(log));

        Optional<Tour_log> foundLog = tourLogService.findById(1L);

        assertEquals(log, foundLog.get());
        verify(tourLogRepository, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        Tour_log log = new Tour_log(LocalDate.now(), LocalTime.now(), "Test comment", Difficulty.EASY, 10.0, 60.0, Rating.GOOD);
        when(tourLogRepository.save(log)).thenReturn(log);

        Tour_log savedLog = tourLogService.save(log);

        assertEquals(log, savedLog);
        verify(tourLogRepository, times(1)).save(log);
    }

    @Test
    public void testDeleteById() {
        tourLogService.deleteById(1L);

        verify(tourLogRepository, times(1)).deleteById(1L);
    }
}