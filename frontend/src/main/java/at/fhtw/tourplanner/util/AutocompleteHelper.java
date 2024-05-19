package at.fhtw.tourplanner.util;

import java.util.List;
import java.util.Map;

public class AutocompleteHelper {

    public List<String> getLabelFromSuggestions(List<Map<String, Object>> suggestions) {
        return suggestions.stream()
                .map(suggestion -> (String) suggestion.get("label"))
                .toList();
    }

    public List<Double> getCoordinatesFromSuggestion(Map<String, Object> suggestion) {
        if (suggestion.containsKey("coordinates")) {
            Object coordinatesObj = suggestion.get("coordinates");
            if (coordinatesObj instanceof List<?>) {
                List<?> coordinatesList = (List<?>) coordinatesObj;
                if (coordinatesList.stream().allMatch(item -> item instanceof Double)) {
                    return (List<Double>) coordinatesList;
                }
            }
        }
        return null;
    }
}