package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.Quay;

import nl.gertjanidema.netex.dataload.dto.StNetexQuay;
import nl.gertjanidema.netex.dataload.dto.StNetexStopPlace;

public class NetexQuayProcessor extends AbstractNetexProcessor {
    public static StNetexQuay process(Quay quay, StNetexStopPlace stopPlace) throws Exception {
        var netexQuay = new StNetexQuay();
        netexQuay.setId(quay.getId());
        netexQuay.setStopPlaceId(stopPlace.getId());
        netexQuay.setVersion(quay.getVersion());
        netexQuay.setName(toString(quay.getName()));
        if (quay.getCentroid() != null) {
            var location = quay.getCentroid().getLocation();
            netexQuay.setX(location.getLongitude().doubleValue());
            netexQuay.setY(location.getLatitude().doubleValue());
        }
        return netexQuay;
    }
}
