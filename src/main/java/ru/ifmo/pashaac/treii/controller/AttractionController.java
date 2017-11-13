package ru.ifmo.pashaac.treii.controller;

import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.pashaac.treii.domain.Venue;
import ru.ifmo.pashaac.treii.domain.foursquare.PlaceType;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.service.CityService;
import ru.ifmo.pashaac.treii.service.VenueService;
import ru.ifmo.pashaac.treii.service.data.MachineLearningService;
import ru.ifmo.pashaac.treii.service.miner.QuadTreeMinerService;

import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 21:12 11.10.17.
 */
@RestController
@RequestMapping("/attraction")
@ApiModel(value = "Attraction manager controller", description = "API for work with project 'Attractions' resources")
@CrossOrigin
public class AttractionController extends DataController {

    @Autowired
    public AttractionController(CityService cityService, VenueService venueService, QuadTreeMinerService quadTreeMinerService, MachineLearningService machineLearningService) {
        super(cityService, venueService, quadTreeMinerService, machineLearningService);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Venue> attractions(@RequestParam double lat, @RequestParam double lng) {
        return data(lat, lng, PlaceType.getAttractions());
    }

    @RequestMapping(value = "/force", method = RequestMethod.GET)
    public List<Venue> attractionsForce(@RequestParam double lat, @RequestParam double lng) {
        attractionsRemove(lat, lng);
        return attractions(lat, lng);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public void attractionsRemove(@RequestParam double lat, @RequestParam double lng) {
        data_remove(lat, lng, PlaceType.getAttractions());
    }

    @RequestMapping(value = "/boundingbox", method = RequestMethod.GET)
    public List<BoundingBox> attractionsBoundingBoxes(@RequestParam double lat, @RequestParam double lng) {
        return data_boundingBoxes(lat, lng, PlaceType.getAttractions());
    }

    @RequestMapping(value = "/grid", method = RequestMethod.GET)
    public List<BoundingBox> attractionsWeightGrid(@RequestParam double lat, @RequestParam double lng) {
        return data_grid(lat, lng, PlaceType.getAttractions());
    }

    @RequestMapping(value = "/attractiveness", method = RequestMethod.GET)
    public List<Venue> attractionsAttractiveness(@RequestParam double lat, @RequestParam double lng) {
        return data_attractiveness(lat, lng, PlaceType.getAttractions());
    }

}
