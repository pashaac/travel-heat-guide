package ru.itmo.travelheatguide.model;

import lombok.Builder;
import org.springframework.data.relational.core.mapping.Embedded;

@Builder
public record BoundingBox(@Embedded(prefix = "south_west_", onEmpty = Embedded.OnEmpty.USE_NULL) Marker southWest,
                          @Embedded(prefix = "north_east_", onEmpty = Embedded.OnEmpty.USE_NULL) Marker northEast) {

}
