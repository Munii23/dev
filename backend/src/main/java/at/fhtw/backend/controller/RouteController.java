package at.fhtw.backend.controller;

import at.fhtw.backend.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public String getRoute(@RequestParam List<Double> startCoordinates, @RequestParam List<Double> endCoordinates) {
        return routeService.getRoute(startCoordinates, endCoordinates);
    }
}