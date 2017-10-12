package ru.ifmo.pashaac.treii.domain.vo;

import javax.persistence.Embeddable;

/**
 * Created by Pavel Asadchiy
 * on 22:56 09.10.17.
 */
@Embeddable
public class BoundingBox {

    private Marker southWest;
    private Marker northEast;

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
