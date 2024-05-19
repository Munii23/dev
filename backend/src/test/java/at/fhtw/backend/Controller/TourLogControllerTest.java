package at.fhtw.backend.Controller;

import at.fhtw.backend.controller.TourLogController;
import at.fhtw.backend.model.Tour_log;
import at.fhtw.backend.repository.TourLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TourLogController.class)
public class TourLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourLogRepository tourLogRepository;

    private Tour_log tourLog1;
    private Tour_log tourLog2;

    @BeforeEach
    public void setup() {
        tourLog1 = new Tour_log();
        tourLog1.setId(1L);
        // set other properties of tourLog1

        tourLog2 = new Tour_log();
        tourLog2.setId(2L);
        // set other properties of tourLog2
    }

    @Test
    public void getAllTourLogTest() throws Exception {
        List<Tour_log> tourLogs = Arrays.asList(tourLog1, tourLog2);
        when(tourLogRepository.findAll()).thenReturn(tourLogs);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tour_log")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(tourLog1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(tourLog2.getId().intValue())));
    }

    @Test
    public void createTourLogTest() throws Exception {
        Tour_log tourLog = new Tour_log();
        tourLog.setId(3L);
        // set other properties of tourLog

        when(tourLogRepository.save(tourLog)).thenReturn(tourLog);

        mockMvc.perform(post("/api/tour_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3, \"otherProperty\":\"value\"}")) // replace with actual JSON representation of tourLog
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(tourLog.getId().intValue())));
    }

    @Test
    public void deleteTourLogTest() throws Exception {
        Long idToDelete = 1L;

        doNothing().when(tourLogRepository).deleteById(idToDelete);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tour_log/" + idToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(tourLogRepository, times(1)).deleteById(idToDelete);
    }
}