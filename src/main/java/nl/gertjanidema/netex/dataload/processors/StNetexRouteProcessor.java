package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.Route;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexRoute;

@Component
public class StNetexRouteProcessor extends AbstractItemProcessor implements ItemProcessor<Route, StNetexRoute> {
 
    @Override
    public StNetexRoute process(Route route) throws Exception {
        var netexRoute = new StNetexRoute();
        netexRoute.setNetexId(route.getId());
        netexRoute.setName(route.getName() != null ? toString(route.getName()) : null);
        netexRoute.setLineRef(route.getLineRef().getValue().getRef());
        netexRoute.setDirectionType(route.getDirectionType().toString());
        return netexRoute;
    }
}