package ru.ifmo.pashaac.treii.service;

import com.google.maps.model.Bounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.BoundingBox;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.service.data.google.GoogleService;

/**
 * Created by Pavel Asadchiy
 * on 1:37 10.10.17.
 */
@Service
public class BoundingBoxService {

    private static final Logger logger = LoggerFactory.getLogger(BoundingBoxService.class);

    @Autowired
    private GoogleService googleService;

    public BoundingBox geolocation(City city) {
        Bounds box = googleService.geocoding(city.getCity() + ", " + city.getCountry()).geometry.bounds;
        return new BoundingBox(new Marker(box.southwest.lat, box.southwest.lng), new Marker(box.northeast.lat, box.northeast.lng));
    }
}
