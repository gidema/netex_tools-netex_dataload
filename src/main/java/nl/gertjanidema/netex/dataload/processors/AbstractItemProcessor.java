package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.LocationStructure;
import org.rutebanken.netex.model.MultilingualString;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractItemProcessor {
    public static String toString(MultilingualString mlString) {
        return (mlString == null ? null : mlString.getContent().get(0).toString());
    }
    
    static double getX(LocationStructure locationStructure) {
        if (locationStructure == null) return 0;
        return locationStructure.getPos().getValue().get(0).doubleValue();
    }
    
    static double getY(LocationStructure locationStructure) {
        if (locationStructure == null) return 0;
        return locationStructure.getPos().getValue().get(1).doubleValue();
    }
}
