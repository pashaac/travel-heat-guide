package ru.ifmo.pashaac.treii.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import ru.ifmo.pashaac.treii.domain.vo.Attractiveness;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.domain.yandex.EstateAdType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Pavel Asadchiy
 * on 22:30 28.10.17.
 */
@Entity
@Table(indexes = {
        @Index(name = "IX_YEID", columnList = "yeid"),
        @Index(name = "IX_LATITUDE", columnList = "latitude"),
        @Index(name = "IX_LONGITUDE", columnList = "longitude")
})
public class YandexEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long yeid; // yandex estate id
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private Marker location;
    private String address;

    private double area;
    private int rooms;
    private long price;
    private String currency;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    private Long buildYear;

    @Enumerated(EnumType.STRING)
    private EstateAdType estateAdType;

    @Column(length = 1024)
    private String additional;

    @JsonBackReference("city-yandex-estate")
    @ManyToOne(targetEntity = City.class, fetch = FetchType.EAGER, optional = false)
    private City city;

    @JsonInclude
    @Transient
    private String iconColor = Attractiveness.neutralColor();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getYeid() {
        return yeid;
    }

    public void setYeid(Long yeid) {
        this.yeid = yeid;
    }

    public Marker getLocation() {
        return location;
    }

    public void setLocation(Marker location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(Long buildYear) {
        this.buildYear = buildYear;
    }

    public EstateAdType getEstateAdType() {
        return estateAdType;
    }

    public void setEstateAdType(EstateAdType estateAdType) {
        this.estateAdType = estateAdType;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    @Override
    public String toString() {
        return "YandexEstate{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", area=" + area +
                ", rooms=" + rooms +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", additional='" + additional + '\'' +
                '}';
    }
}
