package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.Version;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexVersion;

@Component
public class StNetexVersionProcessor extends AbstractItemProcessor implements ItemProcessor<Version, StNetexVersion> {
 
    @Override
    public StNetexVersion process(Version version) throws Exception {
        var netexVersion = new StNetexVersion();
        netexVersion.setNetexId(version.getId());
        netexVersion.setVersion(version.getVersion());
        netexVersion.setVersionType(version.getVersionType().toString());
        netexVersion.setModification(getModification(version));
        netexVersion.setStartDate(version.getStartDate());
        netexVersion.setEndDate(version.getEndDate());
        return netexVersion;
    }
    
    private static String getModification(Version version) {
        
        if (version.getModification() == null) return null;
        return version.getModification().value();
    }
}