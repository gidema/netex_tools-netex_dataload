package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.TransportAdministrativeZone;

import nl.gertjanidema.netex.dataload.dto.StNetexAdminZone;

public class NetexAdminZoneProcessor extends AbstractNetexProcessor {
 
    public static StNetexAdminZone process(TransportAdministrativeZone adminZone) throws Exception {
        var netexAdminZone = new StNetexAdminZone();
        netexAdminZone.setId(adminZone.getId());
        netexAdminZone.setName(toString(adminZone.getName()));
        netexAdminZone.setShortName(toString(adminZone.getShortName()));
        netexAdminZone.setDescription(toString(adminZone.getDescription()));
        return netexAdminZone;
    }
}