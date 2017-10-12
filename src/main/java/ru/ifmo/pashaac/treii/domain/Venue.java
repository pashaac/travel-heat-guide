package ru.ifmo.pashaac.treii.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fi.foyt.foursquare.api.entities.CompactVenue;
import ru.ifmo.pashaac.treii.domain.foursquare.PlaceType;
import ru.ifmo.pashaac.treii.domain.vo.Marker;

import javax.persistence.*;

/**
 * Created by Pavel Asadchiy
 * on 22:48 10.10.17.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "fid", "address"})})
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fid;
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private Marker location;
    @Enumerated(value = EnumType.STRING)
    private PlaceType type;
    private String address;

    private long checkinsCount;
    private long usersCount;

    @JsonBackReference("city-venue")
    @ManyToOne(targetEntity = City.class, fetch = FetchType.EAGER, optional = false)
    private City city;

    public Venue() {
    }

    public Venue(CompactVenue venue) {
        location = new Marker(venue.getLocation().getLat(), venue.getLocation().getLng());
        name = venue.getName();
        fid = venue.getId();
        address = venue.getLocation().getCity() + ", " + venue.getLocation().getAddress();
        checkinsCount = venue.getStats().getCheckinsCount();
        usersCount = venue.getStats().getUsersCount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Marker getLocation() {
        return location;
    }

    public void setLocation(Marker location) {
        this.location = location;
    }

    public PlaceType getType() {
        return type;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCheckinsCount() {
        return checkinsCount;
    }

    public void setCheckinsCount(long checkinsCount) {
        this.checkinsCount = checkinsCount;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
