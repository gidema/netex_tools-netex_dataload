package nl.gertjanidema.netex.dataload.processors;

import java.util.Objects;

import org.rutebanken.netex.model.PointOnRoute;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexPointOnRoute;

@Component
public class StNetexPointOnRouteProcessor implements ItemProcessor<PointOnRoute, StNetexPointOnRoute> {

    @Override
    public StNetexPointOnRoute process(PointOnRoute por) throws Exception {
        var stPor = new StNetexPointOnRoute();
        stPor.setNetexId(por.getId());
        stPor.setVersion(por.getVersion());
        stPor.setRoutePointRef(por.getPointRef().getValue().getRef());
            // TODO Log when getOrder is null
        stPor.setSequence(Objects.requireNonNullElse(por.getOrder(), 0).intValue());
        return stPor;
    }

}
