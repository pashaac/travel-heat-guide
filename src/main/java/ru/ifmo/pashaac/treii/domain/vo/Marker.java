package ru.ifmo.pashaac.treii.domain.vo;

import javax.persistence.Embeddable;

/**
 * Created by Pavel Asadchiy
 * on 0:38 10.10.17.
 */
@Embeddable
public class Marker {

    private double latitude;
    private double longitude;

    public Marker(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Marker() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
