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

    @JsonManagedReference("city-venue")
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venue> venues = new ArrayList<>();

    public City() {
    }

    public City(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Marker getSouthWest() {
        return southWest;
    }

    public void setSouthWest(Marker southWest) {
        this.southWest = southWest;
    }

    public Marker getNorthEast() {
        return northEast;
    }

    public void setNorthEast(Marker northEast) {
        this.northEast = northEast;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }
}
