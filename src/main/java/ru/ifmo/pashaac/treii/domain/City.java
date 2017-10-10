package ru.ifmo.pashaac.treii.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.ifmo.pashaac.treii.domain.vo.Marker;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 22:55 09.10.17.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"city", "country"}))
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String country;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "southWestLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "southWestLongitude"))
    })
    private Marker southWest;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "northEastLatitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "northEastLongitude"))
    })
    private Marker northEast;

    @JsonManagedReference("city-boundingBox")
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoundingBox> boundingBoxes = new ArrayList<>();

    public City() {
    }

    public City(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Marker getSouthWest() {
        return southWest;
    }

    public Marker getNorthEast() {
        return northEast;
    }

    public void setSouthWest(Marker southWest) {
        this.southWest = southWest;
    }

    public void setNorthEast(Marker northEast) {
        this.northEast = northEast;
    }
}
