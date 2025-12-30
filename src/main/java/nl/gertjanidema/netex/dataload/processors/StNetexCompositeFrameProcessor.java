package nl.gertjanidema.netex.dataload.processors;

import java.util.LinkedList;
import java.util.Optional;

import org.rutebanken.netex.model.CompositeFrame;
import org.rutebanken.netex.model.GeneralFrame;
import org.rutebanken.netex.model.ResourceFrame;
import org.rutebanken.netex.model.ServiceFrame;
import org.rutebanken.netex.model.SiteFrame;
import org.rutebanken.netex.model.Version;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.StNetexCompositeFrame;
import nl.gertjanidema.netex.dataload.dto.StNetexVersion;

@Component
public class StNetexCompositeFrameProcessor extends AbstractItemProcessor implements ItemProcessor<CompositeFrame, StNetexCompositeFrame> {
    @Inject StNetexResourceFrameProcessor resourceFrameProcessor;
    @Inject StNetexServiceFrameProcessor serviceFrameProcessor;
    @Inject StNetexSiteFrameProcessor siteFrameProcessor;
    @Inject StNetexGeneralFrameProcessor generalFrameProcessor;
    @Inject StNetexVersionProcessor versionProcessor;

    @Override
    public StNetexCompositeFrame process(CompositeFrame compositeFrame) throws Exception {
        var nCompositeFrame = new StNetexCompositeFrame();
        Optional.ofNullable(compositeFrame.getTypeOfFrameRef()).ifPresent(ref-> {
            nCompositeFrame.setRef(ref.getRef());
            nCompositeFrame.setRefVersion(ref.getVersion());
        });
        if (compositeFrame.getFrames() != null) {
            for(var commonFrame : compositeFrame.getFrames().getCommonFrame()) {
                if (commonFrame.getDeclaredType().equals(ResourceFrame.class)) {
                    var nResourceFrame = resourceFrameProcessor.process((ResourceFrame)commonFrame.getValue());
                    nResourceFrame.setParentFrame(nCompositeFrame);
                    nCompositeFrame.setResourceFrame(nResourceFrame);
                }
                else if (commonFrame.getDeclaredType().equals(ServiceFrame.class)) {
                    var nServiceFrame = serviceFrameProcessor.process((ServiceFrame)commonFrame.getValue());
                    nServiceFrame.setParentFrame(nCompositeFrame);
                    nCompositeFrame.setServiceFrame(nServiceFrame);
                }
                else if (commonFrame.getDeclaredType().equals(SiteFrame.class)) {
                    var nSiteFrame = siteFrameProcessor.process((SiteFrame)commonFrame.getValue());
                    nSiteFrame.setParentFrame(nCompositeFrame);
                    nCompositeFrame.setSiteFrame(nSiteFrame);
                }
                else if (commonFrame.getDeclaredType().equals(GeneralFrame.class)) {
                    var nGeneralFrame = generalFrameProcessor.process((GeneralFrame)commonFrame.getValue());
                    nGeneralFrame.setParentFrame(nCompositeFrame);
                    nCompositeFrame.setGeneralFrame(nGeneralFrame);
                }
            }
        }
        var nVersions = new LinkedList<StNetexVersion>();
        if (compositeFrame.getVersions() != null) {
            for (var version : compositeFrame.getVersions().getVersionRefOrVersion()) {
                if (version instanceof Version) {
                    nVersions.add(versionProcessor.process((Version)version));
                }
            }
        }
        nCompositeFrame.setVersions(nVersions);
        return nCompositeFrame;
    }
}