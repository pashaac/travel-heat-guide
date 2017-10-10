package ru.ifmo.pashaac.treii.service;

import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.BoundingBox;
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
        Marker center = center(box);
        return new BoundingBox(new Marker(center.getLatitude(), box.getSouthWest().getLongitude()), new Marker(box.getNorthEast().getLatitude(), center.getLongitude()));
    }

    public BoundingBox leftDownBoundingBox(BoundingBox box) {
        Marker center = center(box);
        return new BoundingBox(new Marker(box.getSouthWest().getLatitude(), box.getSouthWest().getLongitude()), new Marker(center.getLatitude(), center.getLongitude()));
    }

    public BoundingBox rightUpBoundingBox(BoundingBox box) {
        Marker center = center(box);
        return new BoundingBox(new Marker(center.getLatitude(), center.getLongitude()), new Marker(box.getNorthEast().getLatitude(), box.getNorthEast().getLongitude()));
    }

    public BoundingBox rightDownBoundingBox(BoundingBox box) {
        Marker center = center(box);
        return new BoundingBox(new Marker(box.getSouthWest().getLatitude(), center.getLongitude()), new Marker(center.getLatitude(), box.getNorthEast().getLongitude()));
    }

    public Marker center(BoundingBox box) {
        return new Marker((box.getNorthEast().getLatitude() + box.getSouthWest().getLatitude()) / 2,
                (box.getNorthEast().getLongitude() + box.getSouthWest().getLongitude()) / 2);
    }

    private Point point(double lat, double lng) {
        return new Point(new DegreeCoordinate(lat), new DegreeCoordinate(lng));
    }

    private Point point(Marker marker) {
        return point(marker.getLatitude(), marker.getLongitude());
    }
}
