package nl.gertjanidema.netex.dataload.processors;

import java.util.ArrayList;
import java.util.List;

import org.rutebanken.netex.model.Quay;
import org.rutebanken.netex.model.StopPlace;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.StNetexQuay;
import nl.gertjanidema.netex.dataload.dto.StNetexStopPlace;

@Component
public class StNetexStopPlaceProcessor extends AbstractItemProcessor implements ItemProcessor<StopPlace, StNetexStopPlace> {

    public StNetexStopPlaceProcessor() {
        super();
    }

    @Inject StNetexQuayProcessor quayProcessor;

    @Override
    public StNetexStopPlace process(StopPlace stopPlace) throws Exception {
        var netexStopPlace = new StNetexStopPlace();
        netexStopPlace.setNetexId(stopPlace.getId());
        netexStopPlace.setVersion(stopPlace.getVersion());
        netexStopPlace.setName(toString(stopPlace.getName()));
        if (stopPlace.getPrivateCode() != null) {
            netexStopPlace.setPrivateCode(stopPlace.getPrivateCode().getValue());
        }
        else if (stopPlace.getPrivateCodes() != null) {
            stopPlace.getPrivateCodes().getPrivateCode().forEach(code -> {
                netexStopPlace.setPrivateCode(code.getValue());
            });
        }
        if (stopPlace.getCentroid() != null) {
            var location = stopPlace.getCentroid().getLocation();
            netexStopPlace.setX(getX(location));
            netexStopPlace.setY(getY(location));
        }
        var quays = stopPlace.getQuays().getQuayRefOrQuay();
        List<StNetexQuay> netexQuays = new ArrayList<>(quays.size());
        quays.forEach(q -> {
            if (q.getDeclaredType() == Quay.class) {
                var quay = (Quay)q.getValue();
                StNetexQuay netexQuay;
                try {
                    netexQuay = quayProcessor.process(quay);
                    netexQuay.setStopPlace(netexStopPlace);
                    netexQuays.add(netexQuay);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        netexStopPlace.setQuays(netexQuays);
        return netexStopPlace;
    }
}
