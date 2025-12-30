package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.TransportAdministrativeZone;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexAdminZone;

@Component
public class StNetexAdminZoneProcessor extends AbstractItemProcessor implements ItemProcessor<TransportAdministrativeZone, StNetexAdminZone> {
 
    @Override
    public StNetexAdminZone process(TransportAdministrativeZone adminZone) throws Exception {
        var netexAdminZone = new StNetexAdminZone();
        netexAdminZone.setNetexId(adminZone.getId());
        netexAdminZone.setVersion(adminZone.getVersion());
        netexAdminZone.setName(toString(adminZone.getName()));
        netexAdminZone.setShortName(toString(adminZone.getShortName()));
        netexAdminZone.setDescription(toString(adminZone.getDescription()));
        return netexAdminZone;
    }
}