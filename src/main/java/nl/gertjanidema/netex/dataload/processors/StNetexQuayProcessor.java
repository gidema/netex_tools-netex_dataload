package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.Quay;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexQuay;

@Component
public class StNetexQuayProcessor extends AbstractItemProcessor implements ItemProcessor<Quay, StNetexQuay> {
    @Override
    public StNetexQuay process(Quay quay) throws Exception {
        var netexQuay = new StNetexQuay();
        netexQuay.setNetexId(quay.getId());
        netexQuay.setVersion(quay.getVersion());
        netexQuay.setName(toString(quay.getName()));
        if (quay.getCentroid() != null) {
            var location = quay.getCentroid().getLocation();
            var it = location.getPos().getValue().iterator();
            netexQuay.setX(it.next().doubleValue());
            netexQuay.setY(it.next().doubleValue());
        }
        return netexQuay;
    }
}
