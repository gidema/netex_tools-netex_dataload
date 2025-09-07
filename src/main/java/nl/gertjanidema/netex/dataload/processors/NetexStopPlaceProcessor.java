package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.StopPlace;

import nl.gertjanidema.netex.dataload.dto.StNetexStopPlace;

public class NetexStopPlaceProcessor extends AbstractNetexProcessor {
    public static StNetexStopPlace process(StopPlace stopPlace) throws Exception {
        var netexStopPlace = new StNetexStopPlace();
        netexStopPlace.setId(stopPlace.getId());
        netexStopPlace.setVersion(stopPlace.getVersion());
        netexStopPlace.setName(toString(stopPlace.getName()));
        netexStopPlace.setPrivateCode(stopPlace.getPrivateCode().getValue());
        if (stopPlace.getCentroid() != null) {
            var location = stopPlace.getCentroid().getLocation();
            netexStopPlace.setX(getX(location));
            netexStopPlace.setY(getY(location));
        }
        return netexStopPlace;
    }
}
