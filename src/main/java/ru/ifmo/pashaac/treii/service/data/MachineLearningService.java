package ru.ifmo.pashaac.treii.service.data;

import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.KMeansLloyd;
import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.initialization.RandomlyGeneratedInitialMeans;
import de.lmu.ifi.dbs.elki.data.Clustering;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.model.KMeansModel;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.ids.DBIDRange;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.datasource.DatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.SquaredEuclideanDistanceFunction;
import de.lmu.ifi.dbs.elki.math.random.RandomFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.Venue;
import ru.ifmo.pashaac.treii.domain.vo.Attractiveness;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.exception.ResourceNotFoundException;
import ru.ifmo.pashaac.treii.service.BoundingBoxService;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Created by Pavel Asadchiy
 * on 22:20 18.10.17.
 */
@Service
public class MachineLearningService {

    private static final Logger logger = LoggerFactory.getLogger(MachineLearningService.class);

    private static final long BOUNDINGBOX_MAX_DIAGONAL = 1000;

    private final BoundingBoxService boundingBoxService;

    private Venue topVenue;
    private Double topVenueBoxRadius;

    @Autowired
    public MachineLearningService(BoundingBoxService boundingBoxService) {
        this.boundingBoxService = boundingBoxService;
    }

    public List<BoundingBox> calculateCityWeightGrid(BoundingBox cityBoundingBox, List<Venue> venues) {
        List<BoundingBox> boundingBoxes = boundingBoxService.split(cityBoundingBox, BOUNDINGBOX_MAX_DIAGONAL);
        logger.info("City boundingbox was split on {} parts", boundingBoxes.size());
        topVenue = venues.stream().max(Comparator.comparing(Venue::getCheckinsCount))
                .orElseThrow(() -> new ResourceNotFoundException("Can't find any venue in the city"));
        topVenueBoxRadius = boundingBoxService.radius(topVenue.getLocation(), cityBoundingBox);
        logger.info("Top venue: {} with {} checkins cover radius {}", topVenue.getName(), topVenue.getCheckinsCount(), topVenueBoxRadius);
        BiFunction<Venue, Double, Double> attractivenessBiFunction = attractivenessFunction();
        boundingBoxes.forEach(boundingBox -> boundingBox.setAttractiveness(venues.parallelStream()
                .mapToDouble(venue -> attractivenessBiFunction.apply(venue, boundingBoxService.radius(venue.getLocation(), boundingBox)))
                .sum()));
        return clustering(boundingBoxes);
    }

    private List<BoundingBox> clustering(List<BoundingBox> boundingBoxes) {
        double[][] data = new double[boundingBoxes.size()][1];
        String[] label = new String[boundingBoxes.size()];
        for (int i = 0; i < boundingBoxes.size(); i++) {
            data[i][0] = boundingBoxes.get(i).getAttractiveness();
            label[i] = "boundingbox#" + i;
        }
        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(data, label);
        Database db = new StaticArrayDatabase(dbc, null);
        db.initialize();
        SquaredEuclideanDistanceFunction dist = SquaredEuclideanDistanceFunction.STATIC;
        RandomlyGeneratedInitialMeans init = new RandomlyGeneratedInitialMeans(RandomFactory.DEFAULT);
        KMeansLloyd<NumberVector> km = new KMeansLloyd<>(dist, Attractiveness.values().length, 0, init);
        Clustering<KMeansModel> c = km.run(db);
        Relation<NumberVector> rel = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);
        DBIDRange ids = (DBIDRange) rel.getDBIDs();
        for (int i = 0; i < c.getAllClusters().size(); i++) {
            for (DBIDIter it = c.getAllClusters().get(i).getIDs().iter(); it.valid(); it.advance()) {
                NumberVector v = rel.get(it);
                final int offset = ids.getOffset(it);
                boundingBoxes.get(offset).setColor(Attractiveness.values()[i].getColor());
                System.out.print(" " + offset);
            }
        }
        return boundingBoxes;
    }

    private BiFunction<Venue, Double, Double> attractivenessFunction() {
        double k = -topVenue.getCheckinsCount() / topVenueBoxRadius;
        return (v, d) ->  Math.max(v.getCheckinsCount() / d, 0.0);
    }


}
