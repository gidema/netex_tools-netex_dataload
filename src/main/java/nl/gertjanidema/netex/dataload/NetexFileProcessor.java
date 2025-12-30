package nl.gertjanidema.netex.dataload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;
import nl.gertjanidema.netex.dataload.dto.StNetexDeliveryRepository;
import nl.gertjanidema.netex.dataload.ndov.NdovSession;
import nl.gertjanidema.netex.dataload.processors.StNetexDeliveryProcesser;

@Component
public class NetexFileProcessor {
    private static Logger LOG = LoggerFactory.getLogger(NetexFileProcessor.class);

//    private PublicationDeliveryStructure delivery;
//    private StNetexDelivery stDelivery;

    @Inject StNetexDeliveryRepository deliveryRepository;
//    @Inject StNetexResponsibilitySetRepository responsibilitySetRepository;
//    @Inject StNetexNetworkRepository networkRepository;
//    @Inject StNetexAdminZoneRepository adminZoneRepository;
//    @Inject StNetexLineRepository lineRepository;
//    @Inject StNetexRouteRepository routeRepository;
//    @Inject StNetexPointOnRouteRepository pointOnRouteRepository;
//    @Inject StNetexPointOnJourneyRepository pointOnJourneyRepository;
//    @Inject StNetexScheduledStopPointRepository scheduledStopPointRepository;
//    @Inject StNetexQuayRepository quayRepository;
//    @Inject StNetexStopPlaceRepository stopPlaceRepository;
    
    @Inject StNetexDeliveryProcesser deliveryProcessor;
//    @Inject StNetexRouteProcessor routeProcessor;
//    @Inject StNetexPointOnRouteProcessor pointOnRouteProcessor;
//    @Inject StNetexLineProcessor lineProcessor;
    
    @Transactional
    public void processData(NdovNetexFileInfo fileInfo, NdovSession session) {
        deliveryRepository.findByFileSetId(fileInfo.getFileSetId()).ifPresent(currentDelivery -> {
            deliveryRepository.delete(currentDelivery);
        });
//        productCategoryRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        responsibilitySetRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        networkRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        adminZoneRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        lineRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        routeRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        pointOnRouteRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        pointOnJourneyRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        stopPlaceRepository.deleteByFileSetId(stDelivery.getFileSetId());
//        quayRepository.deleteByFileSetId(stDelivery.getFileSetId());
        File file;
        try {
            file = session.getFile(Path.of(fileInfo.getDirectory()), fileInfo.getFileName());
            var delivery = readFile(file);
            var nDelivery = deliveryProcessor.process(delivery);
            nDelivery.setFileInfo(fileInfo);
            deliveryRepository.save(nDelivery);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
