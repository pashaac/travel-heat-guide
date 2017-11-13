package ru.ifmo.pashaac.treii.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.YandexEstate;

import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 23:16 01.11.17.
 */
public interface YandexEstateRepository extends CrudRepository<YandexEstate, Long> {
    YandexEstate findByYeid(Long yeid);
    List<YandexEstate> findByCity(City city);
}
