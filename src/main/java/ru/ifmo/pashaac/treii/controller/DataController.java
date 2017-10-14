package ru.ifmo.pashaac.treii.controller;

import org.springframework.util.CollectionUtils;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.Venue;
import ru.ifmo.pashaac.treii.domain.foursquare.PlaceType;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.service.CityService;
import ru.ifmo.pashaac.treii.service.GeolocationService;
import ru.ifmo.pashaac.treii.service.VenueService;
import ru.ifmo.pashaac.treii.service.miner.QuadTreeMinerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Pavel Asadchiy
 * on 0:05 13.10.17.
 */
public abstract class DataController {

    private final CityService cityService;
    private final VenueService venueService;
    private final QuadTreeMinerService quadTreeMinerService;
    private final GeolocationService geolocationService;

    DataController(CityService cityService, VenueService venueService, QuadTreeMinerService quadTreeMinerService, GeolocationService geolocationService) {
        this.cityService = cityService;
        this.venueService = venueService;
        this.quadTreeMinerService = quadTreeMinerService;
        this.geolocationService = geolocationService;
    }

    List<Venue> data(double lat, double lng, List<PlaceType> placeTypes) {
        City city = cityService.localization(lat, lng);
        List<PlaceType> placeTypesToMine = new ArrayList<>();
        List<Venue> venues = new ArrayList<>();
        for (PlaceType placeType : placeTypes) {
            List<Venue> placeTypeVenues = venueService.findVenuesByCityAndType(city, placeType);
            if (CollectionUtils.isEmpty(placeTypeVenues)) {
                placeTypesToMine.add(placeType);
            } else {
                venues.addAll(placeTypeVenues);
            }
        }
        venues.addAll(quadTreeMinerService.mine(city, placeTypesToMine));
        return venues;
    }

    void data_remove(double lat, double lng, List<PlaceType> placeTypes) {
        City city = cityService.localization(lat, lng);
        placeTypes.forEach(placeType -> venueService.removeVenueByCityAndType(city, placeType));
    }

    List<BoundingBox> data_boundingBoxes(double lat, double lng, List<PlaceType> placeTypes) {
        City city = cityService.localization(lat, lng);
        List<Venue> reverseVenues = city.getVenues().stream()
                .filter(venue -> placeTypes.contains(venue.getType()))
                .collect(Collectors.toList());
        return quadTreeMinerService.reverseMine(city.getBoundingBox(), reverseVenues);
    }
}
