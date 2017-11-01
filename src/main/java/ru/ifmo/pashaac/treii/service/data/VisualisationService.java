package ru.ifmo.pashaac.treii.service.data;

import de.lmu.ifi.dbs.elki.visualization.visualizers.histogram.ColoredHistogramVisualizer;
import de.lmu.ifi.dbs.elki.visualization.visualizers.visunproj.XYPlotVisualization;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.Venue;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Pavel Asadchiy
 * on 22:33 28.10.17.
 */
@Service
public class VisualisationService {

    public void createCheckinsHistogram(List<Venue> venues) {
        List<Venue> checkinsSortedVenues = venues.stream()
                .sorted(Comparator.comparing(Venue::getCheckinsCount))
                .collect(Collectors.toList());
        ColoredHistogramVisualizer visualizer = new ColoredHistogramVisualizer();
        XYPlotVisualization xyPlotVisualization = new XYPlotVisualization();
        xyPlotVisualization.processNewResult();

    }

}
