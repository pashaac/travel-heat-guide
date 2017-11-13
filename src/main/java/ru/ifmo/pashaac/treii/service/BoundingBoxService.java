package ru.ifmo.pashaac.treii.service;

import com.grum.geocalc.BoundingArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Pavel Asadchiy
 * on 1:37 10.10.17.
 */
@Service
public class BoundingBoxService {

    private static final Logger logger = LoggerFactory.getLogger(BoundingBoxService.class);

    private static final long BOUNDING_BOX_MAX_DIAGONAL = 1_000_000;

    private final GeoMathService geoMathService;

    @Autowired
    public BoundingBoxService(GeoMathService geoMathService) {
        this.geoMathService = geoMathService;
    }

    public List<BoundingBox> split(BoundingBox boundingBox) {
        List<BoundingBox> boundingBoxes = new ArrayList<>(4);
        boundingBoxes.add(geoMathService.leftDownBoundingBox(boundingBox));
        boundingBoxes.add(geoMathService.leftUpBoundingBox(boundingBox));
        boundingBoxes.add(geoMathService.rightDownBoundingBox(boundingBox));
        boundingBoxes.add(geoMathService.rightUpBoundingBox(boundingBox));
        return boundingBoxes;
    }

    public boolean contains(BoundingBox boundingBox, Marker point) {
        BoundingArea boundingArea = new BoundingArea(geoMathService.point(boundingBox.getNorthEast()), geoMathService.point(boundingBox.getSouthWest()));
        return boundingArea.isContainedWithin(geoMathService.point(point));
    }

    public List<BoundingBox> split(BoundingBox boundingBox, double boundingBoxMaxDiagonal) {
        if (geoMathService.diagonal(boundingBox) > boundingBoxMaxDiagonal) {
            return split(boundingBox).stream()
                    .flatMap(box -> split(box, boundingBoxMaxDiagonal).stream())
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(boundingBox);
    }

    public double radius(Marker marker, BoundingBox boundingBox) {
        return Math.max(
                Math.max(geoMathService.distance(marker, geoMathService.getNorthWest(boundingBox)), geoMathService.distance(marker, geoMathService.getSouthEast(boundingBox))),
                Math.max(geoMathService.distance(marker, boundingBox.getSouthWest()), geoMathService.distance(marker, boundingBox.getNorthEast())));
    }

}
