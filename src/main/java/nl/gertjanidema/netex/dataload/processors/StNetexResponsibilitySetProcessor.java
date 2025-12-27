package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.ResponsibilitySet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexResponsibilitySet;

@Component
public class StNetexResponsibilitySetProcessor extends AbstractItemProcessor implements ItemProcessor<ResponsibilitySet, StNetexResponsibilitySet> {
 
    @Override
    public StNetexResponsibilitySet process(ResponsibilitySet responsibilitySet) throws Exception {
        var netexResponsibilitySet = new StNetexResponsibilitySet();
        netexResponsibilitySet.setNetexId(responsibilitySet.getId());
        netexResponsibilitySet.setVersion(responsibilitySet.getVersion());
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