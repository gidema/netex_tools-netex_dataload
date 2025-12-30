package nl.gertjanidema.netex.dataload.processors;

import java.util.Optional;

import org.rutebanken.netex.model.GeneralFrame;
import org.rutebanken.netex.model.Network;
import org.rutebanken.netex.model.TransportAdministrativeZone;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.StNetexGeneralFrame;

@Component
public class StNetexGeneralFrameProcessor extends AbstractItemProcessor implements ItemProcessor<GeneralFrame, StNetexGeneralFrame> {
    @Inject StNetexNetworkProcessor networkProcessor;
    @Inject StNetexAdminZoneProcessor adminZoneProcessor;

    @Override
    public StNetexGeneralFrame process(GeneralFrame frame) throws Exception {
        var nFrame = new StNetexGeneralFrame();
        Optional.ofNullable(frame.getTypeOfFrameRef()).ifPresent(ref-> {
            nFrame.setRef(ref.getRef());
            nFrame.setRefVersion(ref.getVersion());
        });
        for(var member : frame.getMembers().getGeneralFrameMemberOrDataManagedObjectOrEntity_Entity()) {
            if (member.getDeclaredType().isAssignableFrom(Network.class)) {
                var network = networkProcessor.process((Network) member.getValue());
                nFrame.setNetwork(network);
            }
            else if (member.getDeclaredType().isAssignableFrom(TransportAdministrativeZone.class)) {
                var adminZone = adminZoneProcessor.process((TransportAdministrativeZone) member.getValue());
                nFrame.setAdministrativeZone(adminZone);
            }
        }
        return nFrame;
    }

}
