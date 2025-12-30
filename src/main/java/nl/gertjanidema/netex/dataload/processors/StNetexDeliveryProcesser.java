package nl.gertjanidema.netex.dataload.processors;

import java.util.LinkedList;

import org.rutebanken.netex.model.CompositeFrame;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.StNetexCompositeFrame;
import nl.gertjanidema.netex.dataload.dto.StNetexDelivery;

@Component
public class StNetexDeliveryProcesser extends AbstractItemProcessor implements ItemProcessor<PublicationDeliveryStructure, StNetexDelivery>{
    @Inject StNetexCompositeFrameProcessor compositeFrameProcessor;
    
    @Override
    public StNetexDelivery process(PublicationDeliveryStructure delivery) throws Exception {
        var stDelivery = new StNetexDelivery();
        stDelivery.setPublicationTimestamp(delivery.getPublicationTimestamp());
        stDelivery.setParticipantRef(delivery.getParticipantRef());
        if (delivery.getDescription() != null) {
            stDelivery.setDescription(toString(delivery.getDescription()));
        }
        var compositeFrames = new LinkedList<StNetexCompositeFrame>();
        for (var frameStructure : delivery.getDataObjects().getCompositeFrameOrCommonFrame()) {
            if (frameStructure.getDeclaredType().equals(CompositeFrame.class)) {
                var compositeFrame = compositeFrameProcessor.process((CompositeFrame) frameStructure.getValue());
                compositeFrame.setDelivery(stDelivery);
                compositeFrames.add(compositeFrame);
            }
        }
        stDelivery.setCompositeFrames(compositeFrames);
        return stDelivery;
    }
}
