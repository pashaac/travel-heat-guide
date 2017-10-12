package ru.ifmo.pashaac.treii.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.Venue;
import ru.ifmo.pashaac.treii.domain.foursquare.PlaceType;

import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 20:47 11.10.17.
 */
public interface VenueRepository extends CrudRepository<Venue, Long> {
    List<Venue> findVenuesByCityAndType(City city, PlaceType placeType);
    void removeVenueByCityAndType(City city, PlaceType placeType);
}
