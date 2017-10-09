package ru.ifmo.pashaac.treii.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.ifmo.pashaac.treii.exception.ResourceNotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 22:55 09.10.17.
 */
@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String country;

    @JsonManagedReference("city-boundingBox")
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoundingBox> boundingBoxes = new ArrayList<>();

    public City(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public City() {
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public List<BoundingBox> getBoundingBoxes() {
        return boundingBoxes;
    }

    public BoundingBox getCityBoundingBox() {
        if (boundingBoxes.isEmpty()) {
            throw new ResourceNotFoundException("Could not find boundingbox for city: " + city);
        }
        return boundingBoxes.get(0);
    }

    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
