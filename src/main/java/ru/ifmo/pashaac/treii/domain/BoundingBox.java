package ru.ifmo.pashaac.treii.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ru.ifmo.pashaac.treii.domain.vo.Marker;

/**
 * Created by Pavel Asadchiy
 * on 22:56 09.10.17.
 */
//@Entity
public class BoundingBox {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "latitude", column = @Column(name = "southWestLatitude")),
//            @AttributeOverride(name = "longitude", column = @Column(name = "southWestLongitude"))
//    })
    private Marker southWest;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "latitude", column = @Column(name = "northEastLatitude")),
//            @AttributeOverride(name = "longitude", column = @Column(name = "northEastLongitude"))
//    })
    private Marker northEast;

    @JsonBackReference("city-boundingBox")
//    @ManyToOne(targetEntity = City.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private City city;

    public BoundingBox(Marker southWest, Marker northEast) {
        this.southWest = southWest;
        this.northEast = northEast;
    }

    public BoundingBox() {
    }

    public Marker getSouthWest() {
        return southWest;
    }

    public Marker getNorthEast() {
        return northEast;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
