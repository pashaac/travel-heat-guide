package ru.ifmo.pashaac.treii.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fi.foyt.foursquare.api.entities.CompactVenue;
import ru.ifmo.pashaac.treii.domain.foursquare.FoursquarePlaceType;
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
    private FoursquarePlaceType type;
    private String address;

    private long checkinsCount;
    private long usersCount;

    @JsonBackReference("boundingBox-venue")
    @ManyToOne(targetEntity = BoundingBox.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private BoundingBox boundingBox;

    @Transient
    private boolean valid;

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

    public String getName() {
        return name;
    }

    public long getCheckinsCount() {
        return checkinsCount;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public FoursquarePlaceType getType() {
        return type;
    }

    public void setType(FoursquarePlaceType type) {
        this.type = type;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
