package nl.gertjanidema.netex.dataload.processors;

import java.util.HashSet;
import java.util.Set;

import org.rutebanken.netex.model.ResponsibilitySet;

import nl.gertjanidema.netex.dataload.dto.StNetexResponsibilitySet;

public class NetexResponsibilitySetProcessor {
 
    public static StNetexResponsibilitySet process(ResponsibilitySet responsibilitySet) throws Exception {
        var netexResponsibilitySet = new StNetexResponsibilitySet();
        netexResponsibilitySet.setId(responsibilitySet.getId());
        netexResponsibilitySet.setName(responsibilitySet.getName() != null ? responsibilitySet.getName().getValue() : null);
        Set<String> roles = new HashSet<>();
        netexResponsibilitySet.setRoles(roles);
        if (responsibilitySet.getRoles() != null) {
            responsibilitySet.getRoles().getResponsibilityRoleAssignment().forEach(role -> {
                if (role.getResponsibleAreaRef() != null) {
                    roles.add(role.getResponsibleAreaRef().getNameOfRefClass());
                }
            });
        }
        return netexResponsibilitySet;
    }
}