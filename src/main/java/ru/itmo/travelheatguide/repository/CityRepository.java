package ru.itmo.travelheatguide.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.travelheatguide.model.City;

import java.util.Optional;

@Repository
public interface CityRepository extends ListCrudRepository<City, Long> {

    @Query("select * from CITY where :lat BETWEEN south_west_latitude AND north_east_latitude AND :lng BETWEEN south_west_longitude AND north_east_longitude")
    Optional<City> findByCoordinatesBetween(double lat, double lng);

    Optional<City> findByNameAndCountryName(String name, String countryName);

}
