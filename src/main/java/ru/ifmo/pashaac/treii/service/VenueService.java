package ru.ifmo.pashaac.treii.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.Venue;
import ru.ifmo.pashaac.treii.domain.foursquare.PlaceType;
import ru.ifmo.pashaac.treii.repository.VenueRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 20:47 11.10.17.
 */
@Service
public class VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Transactional
    public List<Venue> save(List<Venue> venues) {
        List<Venue> savedVenues = new ArrayList<>(venues.size());
        venueRepository.save(venues).forEach(savedVenues::add);
        return savedVenues;
    }

    @Transactional(readOnly = true)
    public List<Venue> findVenuesByCityAndType(City city, PlaceType placeType) {
        return venueRepository.findVenuesByCityAndType(city, placeType);
    }

    @Transactional
    public void removeVenueByCityAndType(City city, PlaceType placeType) {
        venueRepository.removeVenueByCityAndType(city, placeType);
    }

}
