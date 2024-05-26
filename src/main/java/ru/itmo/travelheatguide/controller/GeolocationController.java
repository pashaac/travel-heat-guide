package ru.itmo.travelheatguide.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.travelheatguide.model.City;
import ru.itmo.travelheatguide.model.Marker;
import ru.itmo.travelheatguide.service.CityService;
import ru.itmo.travelheatguide.service.GeolocationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/geolocation")
public class GeolocationController {

    private final GeolocationService geolocationService;
    private final CityService cityService;

    @GetMapping(value = "/coordinates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<City> geolocate(@RequestParam(value = "lat") Double lat, @RequestParam(value = "lng") Double lng) {
        log.info("geolocate.enter; lat={}, lng={}", lat, lng);
        City city = cityService.getByCoordinates(new Marker(lat, lng))
                .orElseGet(() -> {
                    City geolocation = geolocationService.geolocate(new Marker(lat, lng));
                    return cityService.create(geolocation);
                });
        log.info("geolocate.exit; id={}, name={}, countryName={}", city.id(), city.name(), city.countryName());
        return ResponseEntity.ok(city);
    }

    @GetMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<City> geolocate(@RequestParam(value = "address") String address) {
        City geolocation = geolocationService.geolocate(address);
        City city = cityService.getByNameAndCountryName(geolocation.name(), geolocation.countryName())
                .orElseGet(() -> cityService.create(geolocation));
        return ResponseEntity.ok(city);
    }

}
