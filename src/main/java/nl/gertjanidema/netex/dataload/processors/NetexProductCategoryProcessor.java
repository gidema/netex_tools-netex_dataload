package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.TypeOfProductCategory;

import nl.gertjanidema.netex.dataload.dto.StNetexProductCategory;

public class NetexProductCategoryProcessor extends AbstractNetexProcessor {
 
    public static StNetexProductCategory process(TypeOfProductCategory category) throws Exception {
        var netexProductCategory = new StNetexProductCategory();
        netexProductCategory.setId(category.getId());
        netexProductCategory.setName(category.getName() != null ? toString(category.getName()) : null);
        var description = category.getDescription();
        if (description != null) {
            netexProductCategory.setDescription(toString(description));
        }
        return netexProductCategory;
    }
}