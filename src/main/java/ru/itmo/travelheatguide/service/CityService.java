package ru.itmo.travelheatguide.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.travelheatguide.model.City;
import ru.itmo.travelheatguide.model.Marker;
import ru.itmo.travelheatguide.repository.CityRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public City create(City city) {
        log.info("create.enter; name={}, countryName={}", city.name(), city.countryName());
        City saved = cityRepository.save(city);
        log.info("create.exit; id={}", saved.id());
        return saved;
    }

    public Optional<City> getByCoordinates(Marker coordinates) {
        return cityRepository.findByCoordinatesBetween(coordinates.latitude(), coordinates.longitude());
    }

    public Optional<City> getByNameAndCountryName(String name, String countryName) {
        log.info("getByNameAndCountryName.enter; name={}, countryName={}", name, countryName);
        Optional<City> city = cityRepository.findByNameAndCountryName(name, countryName);
        log.info("getByNameAndCountryName.exit; id={}", city.map(City::id));
        return city;
    }

}
