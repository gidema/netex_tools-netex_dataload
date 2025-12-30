package nl.gertjanidema.netex.dataload.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.rutebanken.netex.model.SiteFrame;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.StNetexSiteFrame;
import nl.gertjanidema.netex.dataload.dto.StNetexStopPlace;

@Component
public class StNetexSiteFrameProcessor extends AbstractItemProcessor implements ItemProcessor<SiteFrame, StNetexSiteFrame> {
    @Inject StNetexStopPlaceProcessor stopPlaceProcessor;

    @Override
    public StNetexSiteFrame process(SiteFrame frame) throws Exception {
        var nFrame = new StNetexSiteFrame();
        Optional.ofNullable(frame.getTypeOfFrameRef()).ifPresent(ref-> {
            nFrame.setRef(ref.getRef());
            nFrame.setRefVersion(ref.getVersion());
        });
        List<StNetexStopPlace> stopPlaces = null;
        if (frame.getStopPlaces() == null) {
            stopPlaces = Collections.emptyList();
        }
        else {
            stopPlaces = new ArrayList<>(frame.getStopPlaces().getStopPlace().size());
            for (var stopPlace : frame.getStopPlaces().getStopPlace()) {
                stopPlaces.add(stopPlaceProcessor.process(stopPlace));
            }
        }
        nFrame.setStopPlaces(stopPlaces);
        return nFrame;
    }

}
