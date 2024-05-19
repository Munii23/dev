package at.fhtw.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RouteService {

    private final RestTemplate restTemplate;

    @Autowired
    public RouteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRoute(List<Double> startCoordinates, List<Double> endCoordinates) {
        String apiKey = "5b3ce3597851110001cf6248b1c45414766948aaa5eeaa7dd33988c1";
        String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + apiKey +
                "&start=" + startCoordinates.get(0) + "," + startCoordinates.get(1) +
                "&end=" + endCoordinates.get(0) + "," + endCoordinates.get(1);
        return restTemplate.getForObject(url, String.class);
    }
}