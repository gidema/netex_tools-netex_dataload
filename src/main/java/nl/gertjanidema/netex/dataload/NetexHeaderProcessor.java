package nl.gertjanidema.netex.dataload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.rutebanken.netex.model.CompositeFrame;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.rutebanken.netex.model.PublicationDeliveryStructure.DataObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.rutebanken.netex.model.ValidBetween;
import org.rutebanken.netex.model.Version;
import org.rutebanken.netex.model.Versions_RelStructure;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;
import nl.gertjanidema.netex.dataload.dto.StNetexVersion;
import nl.gertjanidema.netex.dataload.dto.StNetexVersionRepository;
import nl.gertjanidema.netex.dataload.ndov.NdovService;
import nl.gertjanidema.netex.dataload.ndov.NdovSession;
import nl.gertjanidema.netex.dataload.processors.AbstractItemProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexVersionProcessor;

@Component
public class NetexHeaderProcessor {
    private static Logger LOG = LoggerFactory.getLogger(NetexHeaderProcessor.class);
    
    @Inject private StNetexVersionRepository versionRepository;
    @Inject private StNetexVersionProcessor versionProcessor;
    
    public void processHeader(NdovNetexFileInfo fileInfo, NdovSession session) {
        File file;
        LOG.info("Processing header of {}", fileInfo.getFileName());
        try {
            file = session.getFile(Path.of(fileInfo.getDirectory()), fileInfo.getFileName());
            var delivery = readFile(file);
            var processor = new StateFullProcessor(fileInfo);
            processor.processDelivery(delivery);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class StateFullProcessor {
        private NdovNetexFileInfo fileInfo;
        private List<StNetexVersion> versions = new ArrayList<>();

        public StateFullProcessor(NdovNetexFileInfo fileInfo) {
            super();
            this.fileInfo = fileInfo;
        }

        private void processDelivery(PublicationDeliveryStructure delivery) {
            fileInfo.setPublicationTimestamp(delivery.getPublicationTimestamp());
            fileInfo.setParticipentRef(delivery.getParticipantRef());
            fileInfo.setDescription(AbstractItemProcessor.toString(delivery.getDescription()));
            processDataObjects(delivery.getDataObjects());
        }
    
        private void processDataObjects(DataObjects dataObjects) {
            var frameCount = dataObjects.getCompositeFrameOrCommonFrame().size();
            dataObjects.getCompositeFrameOrCommonFrame().forEach(frameStructure -> {
                if (frameStructure.getDeclaredType().equals(CompositeFrame.class)) {
                    var frame = (CompositeFrame) frameStructure.getValue();
                    var ref = frame.getTypeOfFrameRef() == null ? null : frame.getTypeOfFrameRef().getRef();
                    if ( frameCount==1 || "BISON:TypeOfFrame:NL_TT_BASELINE".equals(ref)) {
                        processBaselineFrame(frame);
                    }
                }
            });
        }
    
        private void processBaselineFrame(CompositeFrame compositeFrame) {
            fileInfo.setVersion(compositeFrame.getVersion());
            fileInfo.setFrameId(compositeFrame.getId());
            processVersions(compositeFrame.getVersions());
            if (versions.isEmpty()) {
                // No versions specified. Try to get them from a ValidBetween element
                processValidBetween(compositeFrame.getValidBetween());
            }
            versionRepository.saveAll(versions);
            if (!versions.isEmpty() && fileInfo.getVersion() != null) {
                versions.forEach(version -> {
                    if (version.getVersion().equals(fileInfo.getVersion())) {
                        fileInfo.setStartDate(version.getStartDate());
                        fileInfo.setEndDate(version.getEndDate());
                    }
                });
            }
            if (fileInfo.getVersion() == null) fileInfo.setVersion("no version");
            if (fileInfo.getStartDate() == null) fileInfo.setStartDate(LocalDateTime.MIN);
            if (fileInfo.getEndDate() == null) fileInfo.setEndDate(LocalDateTime.MAX);
        }
    
        private void processValidBetween(List<ValidBetween> validBetween) {
            if (validBetween == null || validBetween.isEmpty()) return;
            var nVersion = new StNetexVersion();
            nVersion.setFileSetId(fileInfo.getFileSetId());
            nVersion.setVersion(fileInfo.getVersion());
            nVersion.setNetexId(fileInfo.getParticipentRef() + ":Version:" + fileInfo.getVersion());
            nVersion.setStartDate(validBetween.get(0).getFromDate());
            nVersion.setEndDate(validBetween.get(0).getToDate());
            versions.add(nVersion);
        } 
    
        private void processVersions(Versions_RelStructure versionStructure) {
            if (versionStructure == null) return;
            versionStructure.getVersionRefOrVersion().forEach(item -> {
                if (item instanceof Version) {
                    try {
                        versions.add(versionProcessor.process((Version)item));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private static PublicationDeliveryStructure readFile(File netexFile) {
        try (
            var is = new FileInputStream(netexFile);
            var streamReader = new GZIPInputStream(is);
        ) {
            var context = JAXBContext.newInstance(PublicationDeliveryStructure.class);
            var unmarshaller = context.createUnmarshaller();
            @SuppressWarnings("unchecked")
            var delivery = ((JAXBElement<PublicationDeliveryStructure>)unmarshaller.unmarshal(streamReader))
                    .getValue();
            return delivery;
        } catch (JAXBException | IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }
}
