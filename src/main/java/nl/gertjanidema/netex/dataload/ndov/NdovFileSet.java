package nl.gertjanidema.netex.dataload.ndov;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;

public class NdovFileSet {
    private String fileSetId;
    private List<NdovNetexFileInfo> fileInfo = new ArrayList<>();
    
    public NdovFileSet(String fileSetId) {
        super();
        this.fileSetId = fileSetId;
    }

    public String getFileSetId() {
        return fileSetId;
    }

    public List<NdovNetexFileInfo> getFileInfo() {
        return fileInfo;
    }

    /**
     * Get the newest file in this fileSet
     * 
     * @return
     */
    public NdovNetexFileInfo getNewest() {
        return fileInfo.stream().max(new NewestFileComparator()).get();
    }
    
    public void addFile(NdovNetexFileInfo info) {
        fileInfo.add(info);
    }
    
    private class NewestFileComparator implements Comparator<NdovNetexFileInfo> {
        @Override
        public int compare(NdovNetexFileInfo nfi1, NdovNetexFileInfo nfi2) {
            return nfi1.getFileName().compareTo(nfi2.getFileName());
        }
    }
}
