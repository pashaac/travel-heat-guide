package ru.ifmo.pashaac.treii.service;

import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;

/**
 * Created by Pavel Asadchiy
 * on 23:00 10.10.17.
 */
@Service
public class GeoMathService {


    public double distance(Marker location1, Marker location2) {
        return EarthCalc.getVincentyDistance(point(location1), point(location2));
    }

    public double diagonal(BoundingBox box) {
        return distance(box.getSouthWest(), box.getNorthEast());
    }

    public BoundingBox leftUpBoundingBox(BoundingBox box) {
        return new BoundingBox(getNorthWest(leftDownBoundingBox(box)), getNorthWest(rightUpBoundingBox(box)));
    }

    public BoundingBox leftDownBoundingBox(BoundingBox box) {
        Marker center = center(box);
        return new BoundingBox(box.getSouthWest(), center);
    }

    public BoundingBox rightUpBoundingBox(BoundingBox box) {
        Marker center = center(box);
        return new BoundingBox(center, box.getNorthEast());
    }

    public BoundingBox rightDownBoundingBox(BoundingBox box) {
        return new BoundingBox(getSouthEast(leftDownBoundingBox(box)), getSouthEast(rightUpBoundingBox(box)));
    }

    public Marker center(BoundingBox box) {
        return new Marker((box.getNorthEast().getLatitude() + box.getSouthWest().getLatitude()) / 2,
                (box.getNorthEast().getLongitude() + box.getSouthWest().getLongitude()) / 2);
    }

    public Point point(double lat, double lng) {
        return new Point(new DegreeCoordinate(lat), new DegreeCoordinate(lng));
    }

    public Point point(Marker marker) {
        return point(marker.getLatitude(), marker.getLongitude());
    }

    public Marker getNorthWest(BoundingBox boundingBox) {
        return new Marker(boundingBox.getNorthEast().getLatitude(), boundingBox.getSouthWest().getLongitude());
    }

    public Marker getSouthEast(BoundingBox boundingBox) {
        return new Marker(boundingBox.getSouthWest().getLatitude(), boundingBox.getNorthEast().getLongitude());
    }
}
