package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.CompositeFrame;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.rutebanken.netex.model.Version;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;
import nl.gertjanidema.netex.dataload.dto.StNetexDelivery;

@Component
public class StNetexDeliveryProcesser extends AbstractItemProcessor {
    public static StNetexDelivery process(PublicationDeliveryStructure delivery, NdovNetexFileInfo fileInfo) {
        var stDelivery = new StNetexDelivery();
        stDelivery.setFileSetId(fileInfo.getFileSetId());
        stDelivery.setFilename(fileInfo.getFileName());
        stDelivery.setPublicationTimestamp(delivery.getPublicationTimestamp());
        stDelivery.setParticipantRef(delivery.getParticipantRef());
        if (delivery.getDescription() != null) {
            stDelivery.setDescription(toString(delivery.getDescription()));
        }
        delivery.getDataObjects().getCompositeFrameOrCommonFrame().forEach(frameStructure -> {
            if (frameStructure.getDeclaredType().equals(CompositeFrame.class)) {
                processCompositeFrame((CompositeFrame) frameStructure.getValue(), stDelivery);
            }
        });
       return stDelivery;
    }

    private static void processCompositeFrame(CompositeFrame frame, StNetexDelivery stDelivery) {
        if (frame.getTypeOfFrameRef() != null && frame.getTypeOfFrameRef().getRef().equals("BISON:TypeOfFrame:NL_TT_BASELINE")) {
            processBaselineFrame(frame, stDelivery);
        }
    }

    private static void processBaselineFrame(CompositeFrame frame, StNetexDelivery stDelivery) {
        frame.getVersions().getVersionRefOrVersion().forEach(object -> {
            if (object instanceof Version) {
                processBaselineVersion((Version)object, stDelivery);
            }
        });
    }

    private static void processBaselineVersion(Version version, StNetexDelivery stDelivery) {
        stDelivery.setBaselineStartDate(version.getStartDate());
        stDelivery.setBaselineEndDate(version.getEndDate());
    }
}
