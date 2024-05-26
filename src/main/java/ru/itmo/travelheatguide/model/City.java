package ru.itmo.travelheatguide.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("CITY")
@Builder(toBuilder = true)
public record City(@Id Long id,
                   String name,
                   String countryName,
                   @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) BoundingBox boundingBox) {

}
