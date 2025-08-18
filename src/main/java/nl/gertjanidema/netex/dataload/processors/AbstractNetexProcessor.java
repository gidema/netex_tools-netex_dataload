package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.MultilingualString;

public abstract class AbstractNetexProcessor {
    static String toString(MultilingualString mlString) {
        return (mlString == null ? null : mlString.getValue());
    }
}
