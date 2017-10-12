package ru.ifmo.pashaac.treii.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.service.GeolocationService;

/**
 * Created by Pavel Asadchiy
 * on 0:34 10.10.17.
 */
@RestController
@RequestMapping("/geolocation")
@CrossOrigin
public class GeolocationController {

    private final GeolocationService geolocationService;

    @Autowired
    public GeolocationController(GeolocationService geolocationService) {
        this.geolocationService = geolocationService;
    }

    @RequestMapping
    public City geolocation(@RequestParam double lat, @RequestParam double lng) {
        City city = geolocationService.geolocation(new Marker(lat, lng));
        BoundingBox boundingBox = geolocationService.geolocation(city);
        city.setSouthWest(boundingBox.getSouthWest());
        city.setNorthEast(boundingBox.getNorthEast());
        return city;
    }
}
