package ru.ifmo.pashaac.treii.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ifmo.pashaac.treii.domain.BoundingBox;

/**
 * Created by Pavel Asadchiy
 * on 20:46 10.10.17.
 */
public interface BoundingBoxRepository extends CrudRepository<BoundingBox, Long> {
}
