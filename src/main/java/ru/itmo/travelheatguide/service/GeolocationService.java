package ru.itmo.travelheatguide.service;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itmo.travelheatguide.model.BoundingBox;
import ru.itmo.travelheatguide.model.City;
import ru.itmo.travelheatguide.model.Marker;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeolocationService {

    private final GoogleService googleService;

    public City geolocate(Marker location) {
        log.info("geolocate.enter; lat={}, lng={}", location.latitude(), location.longitude());
        GeocodingResult geocoding = googleService.geocoding(location);
        String city = extractCity(geocoding)
                .orElseThrow(() -> new IllegalStateException(String.format("Failed to determine city by coordinates (%s, %s)", location.latitude(), location.longitude())));
        String country = extractCountry(geocoding)
                .orElseThrow(() -> new IllegalStateException(String.format("Failed to determine country by coordinates (%s, %s)", location.latitude(), location.longitude())));
        log.info("geolocation.exit; city={}, country={}", city, country);
        return City.builder()
                .name(city)
                .countryName(country)
                .boundingBox(makeBoundingBox(geocoding.geometry))
                .build();
    }

    public City geolocate(String address) {
        log.info("geolocate.enter; address={}", address);
        GeocodingResult geocoding = googleService.geocoding(address);
        String city = extractCity(geocoding)
                .orElseThrow(() -> new IllegalStateException(String.format("Failed to determine city by address %s", address)));
        String country = extractCountry(geocoding)
                .orElseThrow(() -> new IllegalStateException(String.format("Failed to determine country by address %s", address)));
        log.info("geolocation.exit; city={}, country={}", city, country);
        return City.builder()
                .name(city)
                .countryName(country)
                .boundingBox(makeBoundingBox(geocoding.geometry))
                .build();
    }

    private BoundingBox makeBoundingBox(Geometry geometry) {
        return BoundingBox.builder()
                .southWest(new Marker(geometry.bounds.southwest.lat, geometry.bounds.southwest.lng))
                .northEast(new Marker(geometry.bounds.northeast.lat, geometry.bounds.northeast.lng))
                .build();
    }


    private Optional<String> extractCity(GeocodingResult geocoding) {
        return googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.LOCALITY)
                .or(() -> googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1))
                .or(() -> googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2));
    }

    private Optional<String> extractCountry(GeocodingResult geocoding) {
        return googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.COUNTRY);
    }

}
