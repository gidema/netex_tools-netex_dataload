package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.ResponsibilitySet;

import nl.gertjanidema.netex.dataload.dto.StNetexResponsibilitySet;

public class NetexResponsibilitySetProcessor extends AbstractNetexProcessor {
 
    public static StNetexResponsibilitySet process(ResponsibilitySet responsibilitySet) throws Exception {
        var netexResponsibilitySet = new StNetexResponsibilitySet();
        netexResponsibilitySet.setId(responsibilitySet.getId());
        netexResponsibilitySet.setName(responsibilitySet.getName() != null ? toString(responsibilitySet.getName()) : null);
        if (responsibilitySet.getRoles() != null) {
            responsibilitySet.getRoles().getResponsibilityRoleAssignment().forEach(role -> {
                var areaRef = role.getResponsibleAreaRef();
                if (areaRef != null && 
                    "TransportAdministrativeZone".equals(areaRef.getNameOfRefClass())) {
                    netexResponsibilitySet.setAdministrativeZone(areaRef.getRef());
                }
            });
        }
        return netexResponsibilitySet;
    }
}