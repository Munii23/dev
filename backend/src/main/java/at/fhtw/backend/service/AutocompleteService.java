package at.fhtw.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AutocompleteService {

    private final RestTemplate restTemplate;

    @Autowired
    public AutocompleteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAutocompleteResults(String query) {
        String apiKey = "5b3ce3597851110001cf6248b1c45414766948aaa5eeaa7dd33988c1";
        String url = "https://api.openrouteservice.org/geocode/autocomplete?api_key=" + apiKey + "&text=" + query;
        return restTemplate.getForObject(url, String.class);
    }
}