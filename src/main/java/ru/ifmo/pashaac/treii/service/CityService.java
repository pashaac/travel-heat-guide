package ru.ifmo.pashaac.treii.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.repository.CityRepository;

import java.util.Optional;

/**
 * Created by Pavel Asadchiy
 * on 1:30 10.10.17.
 */
@Service
public class CityService {

    private final CityRepository cityRepository;
    private final GeolocationService geolocationService;

    @Autowired
    public CityService(CityRepository cityRepository, GeolocationService geolocationService) {
        this.cityRepository = cityRepository;
        this.geolocationService = geolocationService;
    }

    @Transactional
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Transactional(readOnly = true)
    public Optional<City> getCity(City city) {
        return Optional.ofNullable(cityRepository.findByCityAndCountry(city.getCity(), city.getCountry()));
    }

    public City localization(double lat, double lng) {
        City cityGeolocation = geolocationService.geolocation(new Marker(lat, lng));
        return getCity(cityGeolocation)
                .orElseGet(() -> {
                    BoundingBox boundingBox = geolocationService.geolocation(cityGeolocation);
                    cityGeolocation.setSouthWest(boundingBox.getSouthWest());
                    cityGeolocation.setNorthEast(boundingBox.getNorthEast());
                    return save(cityGeolocation);
                });
    }

}
