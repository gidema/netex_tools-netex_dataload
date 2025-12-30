package nl.gertjanidema.netex.dataload.processors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import org.rutebanken.netex.model.Line;
import org.rutebanken.netex.model.Line_VersionStructure;
import org.rutebanken.netex.model.LinkSequence_VersionStructure;
import org.rutebanken.netex.model.Route;
import org.rutebanken.netex.model.ServiceFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.StNetexLine;
import nl.gertjanidema.netex.dataload.dto.StNetexRoute;
import nl.gertjanidema.netex.dataload.dto.StNetexScheduledStopPoint;
import nl.gertjanidema.netex.dataload.dto.StNetexServiceFrame;

@Component
public class StNetexServiceFrameProcessor extends AbstractItemProcessor implements ItemProcessor<ServiceFrame, StNetexServiceFrame> {
    private static Logger LOG = LoggerFactory.getLogger(StNetexServiceFrameProcessor.class);

    @Inject StNetexScheduledStopPointProcessor scheduledStopPointProcessor;
    @Inject StNetexLineProcessor lineProcessor;
    @Inject StNetexRouteProcessor routeProcessor;

    @Override
    public StNetexServiceFrame process(ServiceFrame frame) throws Exception {
        var nFrame = new StNetexServiceFrame();
        Optional.ofNullable(frame.getTypeOfFrameRef()).ifPresent(ref-> {
            nFrame.setRef(ref.getRef());
            nFrame.setRefVersion(ref.getVersion());
        });
        if (frame.getLines() != null) {
            var lines = new ArrayList<StNetexLine>(frame.getLines().getLine_Dummy().size());
            for (var lineDummy : frame.getLines().getLine_Dummy()) {
                var line = (Line_VersionStructure)lineDummy.getValue();
                if (line instanceof Line) {
                    lines.add(lineProcessor.process((Line) line));
                }
                else LOG.info("Unprocessed line type: {}", line.getClass().getName());
            }
            nFrame.setLines(lines);
        }
        if (frame.getScheduledStopPoints() != null) {
            var scheduledStopPoints = new LinkedList<StNetexScheduledStopPoint>();
            for(var ssp : frame.getScheduledStopPoints().getScheduledStopPoint()) {
                scheduledStopPoints.add(scheduledStopPointProcessor.process(ssp));
            }
            nFrame.setScheduledStopPoints(scheduledStopPoints);
        }
        if (frame.getRoutes() != null) {
            var routes = new LinkedList<StNetexRoute>();
            for(var dummyRoute : frame.getRoutes().getRoute_Dummy()) {
                var route = (LinkSequence_VersionStructure)dummyRoute.getValue();
                if (route instanceof Route) {
                    routes.add(routeProcessor.process((Route) route));
                }
                else LOG.info("Unprocessed route type: {}", route.getClass().getName());
            }
            nFrame.setRoutes(routes);
        }
        return nFrame;
    }

}
