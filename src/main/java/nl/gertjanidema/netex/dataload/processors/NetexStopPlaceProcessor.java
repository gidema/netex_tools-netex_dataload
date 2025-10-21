package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.StopPlace;

import nl.gertjanidema.netex.dataload.dto.StNetexStopPlace;

public class NetexStopPlaceProcessor extends AbstractNetexProcessor {
    public static StNetexStopPlace process(StopPlace stopPlace) throws Exception {
        var netexStopPlace = new StNetexStopPlace();
        netexStopPlace.setId(stopPlace.getId());
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
        return netexStopPlace;
    }
}
