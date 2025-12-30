package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.TypeOfProductCategory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexProductCategory;

@Component
public class StNetexProductCategoryProcessor extends AbstractItemProcessor implements ItemProcessor<TypeOfProductCategory, StNetexProductCategory> {
 
    @Override
    public StNetexProductCategory process(TypeOfProductCategory category) throws Exception {
        var netexProductCategory = new StNetexProductCategory();
        netexProductCategory.setNetexId(category.getId());
        netexProductCategory.setVersion(category.getVersion());
        netexProductCategory.setName(category.getName() != null ? toString(category.getName()) : null);
        var description = category.getDescription();
        if (description != null) {
            netexProductCategory.setDescription(toString(description));
        }
        return netexProductCategory;
    }
}