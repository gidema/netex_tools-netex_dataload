package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.Route;

import nl.gertjanidema.netex.dataload.dto.StNetexRoute;

public class NetexRouteProcessor extends AbstractNetexProcessor {
 
    public static StNetexRoute process(Route route) throws Exception {
        var netexRoute = new StNetexRoute();
        netexRoute.setId(route.getId());
        netexRoute.setName(route.getName() != null ? toString(route.getName()) : null);
        netexRoute.setLineRef(route.getLineRef().getValue().getRef());
        netexRoute.setDirectionType(route.getDirectionType().toString());
        return netexRoute;
    }
}