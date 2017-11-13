package ru.ifmo.pashaac.treii.service.miner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.Venue;
import ru.ifmo.pashaac.treii.domain.foursquare.PlaceType;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.service.BoundingBoxService;
import ru.ifmo.pashaac.treii.service.VenueService;
import ru.ifmo.pashaac.treii.service.data.foursquare.FoursquareService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Pavel Asadchiy
 * on 23:18 09.10.17.
 */
@Service
public class QuadTreeMinerService {

    private static final Logger logger = LoggerFactory.getLogger(QuadTreeMinerService.class);

    private final BoundingBoxService boundingBoxService;
    private final FoursquareService foursquareService;
    private final VenueService venueService;

    @Autowired
    public QuadTreeMinerService(BoundingBoxService boundingBoxService, FoursquareService foursquareService, VenueService venueService) {
        this.boundingBoxService = boundingBoxService;
        this.foursquareService = foursquareService;
        this.venueService = venueService;
    }

    @Transactional
    public List<Venue> mine(City city, List<PlaceType> placeTypes) {
        if (CollectionUtils.isEmpty(placeTypes)) {
            return Collections.emptyList();
        }
        final String foursquareCategoryIds = placeTypes.stream()
                .map(PlaceType::getCategoryIds).collect(Collectors.joining(","));
        final String foursquareReadablePlaceTypes = placeTypes.stream()
                .map(PlaceType::name).collect(Collectors.joining(",", "[", "]"));
        long startTime = System.currentTimeMillis();
        List<Venue> venues = new ArrayList<>();
        Queue<BoundingBox> boxQueue = new ArrayDeque<>();
        boxQueue.add(new BoundingBox(city.getSouthWest(), city.getNorthEast()));
        int ind = 0;
        int apiCallCounter = 0;
        while (!boxQueue.isEmpty()) {
            logger.info("Trying to get places {} for boundingbox #{}...", foursquareReadablePlaceTypes, ind++);
            BoundingBox boundingBox = boxQueue.poll();
            Optional<List<Venue>> boundingBoxAttractions = foursquareService.search(boundingBox, foursquareCategoryIds);
            ++apiCallCounter;
            if (!boundingBoxAttractions.isPresent()) {
                continue;
            }
            if (FoursquareService.VENUE_MAX_SEARCH == boundingBoxAttractions.get().size()) {
                logger.info("Split bounding box, because was searched: {} places", FoursquareService.VENUE_MAX_SEARCH);
                boxQueue.addAll(boundingBoxService.split(boundingBox));
                continue;
            }
            List<Venue> validFoursquareVenues = boundingBoxAttractions.get().stream()
                    .filter(foursquareService::isValidVenue)
                    .peek(attraction -> attraction.setCity(city))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(validFoursquareVenues)) {
                logger.info("Was searched 0 foursquare places after filtering, skip this boundingbox");
                continue;
            }
            venues.addAll(validFoursquareVenues);
            logger.info("Was searched: {} foursquare venues, after filtering: {}", boundingBoxAttractions.get().size(), validFoursquareVenues.size());
        }
        logger.info("API called approximately: {} times", apiCallCounter);
        logger.info("Total venues was searched and saved: {}", venueService.save(venues).size());
        logger.info("Venues map was created in time: {} ms", System.currentTimeMillis() - startTime);
        return venues;
    }

    public List<BoundingBox> reverseMine(BoundingBox cityBoundingBox, List<Marker> places, int venuesCountInBox) {
        Queue<BoundingBox> boxQueue = new ArrayDeque<>();
        boxQueue.add(cityBoundingBox);
        List<BoundingBox> boundingBoxes = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        while (!boxQueue.isEmpty()) {
            logger.debug("Boundingbox collection size: {}", boundingBoxes.size());
            BoundingBox boundingBox = boxQueue.poll();
            int boundingBoxPlaces = 0;
            for (Marker place : places) {
                if (boundingBoxService.contains(boundingBox, place)) {
                    ++boundingBoxPlaces;
                }
                if (venuesCountInBox == boundingBoxPlaces) {
                    break;
                }
            }
            if (boundingBoxPlaces < venuesCountInBox) {
                boundingBoxes.add(boundingBox);
            }
            if (boundingBoxPlaces == venuesCountInBox) {
                boxQueue.addAll(boundingBoxService.split(boundingBox));
            }
        }
        logger.info("BoundingBox map with {} boundingboxes was created in time: {} ms", boundingBoxes.size(), System.currentTimeMillis() - startTime);
        return boundingBoxes;
    }

}
