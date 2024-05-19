package at.fhtw.tourplanner.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class DisplayHelper {

    public void displayInstructions(String jsonResult) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(jsonResult, Map.class);

            List<Map<String, Object>> features = (List<Map<String, Object>>) map.get("features");

            for (Map<String, Object> feature : features) {
                Map<String, Object> properties = (Map<String, Object>) feature.get("properties");
                List<Map<String, Object>> segments = (List<Map<String, Object>>) properties.get("segments");

                for (Map<String, Object> segment : segments) {
                    List<Map<String, Object>> steps = (List<Map<String, Object>>) segment.get("steps");

                    for (Map<String, Object> step : steps) {
                        String instruction = (String) step.get("instruction");
                        System.out.println(instruction);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
