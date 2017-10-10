package ru.ifmo.pashaac.treii.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.ifmo.pashaac.treii.domain.vo.Marker;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 22:56 09.10.17.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"southWestLatitude", "southWestLongitude", "northEastLatitude", "northEastLongitude"}))
public class BoundingBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @JsonBackReference("city-boundingBox")
    @ManyToOne(targetEntity = City.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private City city;

    @JsonManagedReference("boundingBox-venue")
    @OneToMany(mappedBy = "boundingBox", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Venue> venues = new ArrayList<>();

    public BoundingBox() {
    }

    public BoundingBox(Marker southWest, Marker northEast) {
        this.southWest = southWest;
        this.northEast = northEast;
    }

    public Marker getSouthWest() {
        return southWest;
    }

    public Marker getNorthEast() {
        return northEast;
    }
}
