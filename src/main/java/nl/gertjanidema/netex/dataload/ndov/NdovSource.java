package nl.gertjanidema.netex.dataload.ndov;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;

public class NdovSource {
    private final String ndovFolder;
    private final String id;
    private final Map<String, NdovFileSet> fileSets = new HashMap<>();

    public NdovSource(String folder) {
        super();
        this.ndovFolder = folder;
        this.id = folder.toUpperCase();
    }

    public String getId() {
        return id;
    }

    public String getNdovFolder() {
        return ndovFolder;
    }
    
    public Map<String, NdovFileSet> getFileSets() {
        return fileSets;
    }

    /**
     * Get the @NdovFileSet for the given fileSetId
     * 
     * @param fileSetId
     * @return The existing fileSet or a new fileSet if it doesn't exist
     */
    public NdovFileSet getFileSet(String fileSetId) {
        return fileSets.computeIfAbsent(fileSetId, f -> new NdovFileSet(f));
    }
    
    /**
     * Get all available files for this source
     * @return
     */
    public List<NdovNetexFileInfo> getAvailableFiles() {
        return fileSets.values().stream().flatMap(fileSet -> fileSet.getFileInfo().stream()).toList();
    }
    
    /**
     * Get the newest files per fileSet
     * @return
     */
    public List<NdovNetexFileInfo> getNewestFiles() {
        return fileSets.values().stream().map(NdovFileSet::getNewest).toList();
    }
}
