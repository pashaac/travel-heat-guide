package ru.ifmo.pashaac.treii.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Created by Pavel Asadchiy
 * on 22:56 09.10.17.
 */
@Embeddable
public class BoundingBox {

    private Marker southWest;
    private Marker northEast;

    @Transient
    @JsonInclude
    private Double attractiveness;

    @Transient
    @JsonInclude
    private String color;

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

    public Double getAttractiveness() {
        return attractiveness;
    }

    public String getColor() {
        return color;
    }

    public void setAttractiveness(Double attractiveness) {
        this.attractiveness = attractiveness;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
