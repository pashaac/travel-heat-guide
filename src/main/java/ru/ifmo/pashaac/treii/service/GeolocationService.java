package ru.ifmo.pashaac.treii.service;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.Bounds;
import com.google.maps.model.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.exception.ResourceNotFoundException;
import ru.ifmo.pashaac.treii.service.data.google.GoogleService;

/**
 * Created by Pavel Asadchiy
 * on 21:45 12.10.17.
 */
@Service
public class GeolocationService {

    private static final Logger logger = LoggerFactory.getLogger(GeolocationService.class);

    private final GoogleService googleService;

    @Autowired
    public GeolocationService(GoogleService googleService) {
        this.googleService = googleService;
    }

    /**
     * @return (city, country)
     */
    public City geolocation(Marker location) {
        GeocodingResult geocoding = googleService.geocoding(location);
        String cityStr = googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.LOCALITY)
                .orElse(googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1)
                        .orElse(googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2)
                                .orElseThrow(() -> new ResourceNotFoundException("Can't determine city geolocation by coordinates "
                                        + "(" + location.getLatitude() + ", " + location.getLongitude() + ")"))));
        String countryStr = googleService.getAddressComponentTypeLongName(geocoding, AddressComponentType.COUNTRY)
                .orElseThrow(() -> new ResourceNotFoundException("Can't determine country geolocation by coordinates "
                        + "(" + location.getLatitude() + ", " + location.getLongitude() + ")"));
        logger.info("Google geolocation method determined city: {}, {}", cityStr, countryStr);
        return new City(cityStr, countryStr);
    }

    public BoundingBox geolocation(City city) {
        Bounds box = googleService.geocoding(String.format("%s, %s", city.getCity(), city.getCountry())).geometry.bounds;
        return new BoundingBox(new Marker(box.southwest.lat, box.southwest.lng), new Marker(box.northeast.lat, box.northeast.lng));
    }

}
