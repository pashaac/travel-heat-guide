package ru.ifmo.pashaac.treii.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.pashaac.treii.domain.BoundingBox;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.service.BoundingBoxService;
import ru.ifmo.pashaac.treii.service.CityService;

/**
 * Created by Pavel Asadchiy
 * on 0:34 10.10.17.
 */
@RestController
@RequestMapping("/geolocation")
@CrossOrigin
public class GeolocationController {

    @Autowired
    private CityService cityService;
    @Autowired
    private BoundingBoxService boundingBoxService;

    @RequestMapping
    public City geolocation(@RequestParam double lat, @RequestParam double lng) {
        City city = cityService.geolocation(new Marker(lat, lng));
        BoundingBox boundingBox = boundingBoxService.geolocation(city);
        city.setCityBoundingBox(boundingBox);
        return city;
    }
}
