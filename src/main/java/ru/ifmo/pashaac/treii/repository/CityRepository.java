package ru.ifmo.pashaac.treii.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.pashaac.treii.domain.City;

/**
 * Created by Pavel Asadchiy
 * on 20:45 10.10.17.
 */
public interface CityRepository extends CrudRepository<City, Long> {
    City findByCityAndCountry(String city, String country);
}
