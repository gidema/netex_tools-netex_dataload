package nl.gertjanidema.netex.dataload.processors;

import java.util.LinkedList;
import java.util.Optional;

import org.rutebanken.netex.model.ResourceFrame;
import org.rutebanken.netex.model.TypeOfProductCategory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.StNetexProductCategory;
import nl.gertjanidema.netex.dataload.dto.StNetexResourceFrame;
import nl.gertjanidema.netex.dataload.dto.StNetexResponsibilitySet;

@Component
public class StNetexResourceFrameProcessor extends AbstractItemProcessor implements ItemProcessor<ResourceFrame, StNetexResourceFrame> {
    @Inject StNetexResponsibilitySetProcessor responsibilitySetProcessor;
    @Inject StNetexProductCategoryProcessor productCategoryProcessor;

    @Override
    public StNetexResourceFrame process(ResourceFrame frame) throws Exception {
        var nFrame = new StNetexResourceFrame();
        Optional.ofNullable(frame.getTypeOfFrameRef()).ifPresent(ref-> {
            nFrame.setRef(ref.getRef());
            nFrame.setRefVersion(ref.getVersion());
        });
        var productCategories = new LinkedList<StNetexProductCategory>();
        if (frame.getTypesOfValue() != null) {
            for(var element : frame.getTypesOfValue().getValueSetOrTypeOfValue()) {
                if (element.getDeclaredType().equals(TypeOfProductCategory.class)) {
                    productCategories.add(productCategoryProcessor.process((TypeOfProductCategory)element.getValue()));
                }
            }
        }
        nFrame.setProductCategories(productCategories);
        var responsibilitySets = new LinkedList<StNetexResponsibilitySet>();
        if (frame.getResponsibilitySets() != null) {
            for (var responsibilitySet :frame.getResponsibilitySets().getResponsibilitySet()) {
                if (responsibilitySet.getName() != null) {
                    responsibilitySets.add(responsibilitySetProcessor.process(responsibilitySet));
                }
            }
        }
        nFrame.setResponsibilitySets(responsibilitySets);
        return nFrame;
    }

}
