package nl.gertjanidema.netex.dataload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.rutebanken.netex.model.CompositeFrame;
import org.rutebanken.netex.model.GeneralFrame;
import org.rutebanken.netex.model.Line;
import org.rutebanken.netex.model.Line_VersionStructure;
import org.rutebanken.netex.model.LinkSequence_VersionStructure;
import org.rutebanken.netex.model.Network;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.rutebanken.netex.model.ResourceFrame;
import org.rutebanken.netex.model.Route;
import org.rutebanken.netex.model.ScheduledStopPoint;
import org.rutebanken.netex.model.ServiceFrame;
import org.rutebanken.netex.model.SiteFrame;
import org.rutebanken.netex.model.StopPlace;
import org.rutebanken.netex.model.TransportAdministrativeZone;
import org.rutebanken.netex.model.TypeOfProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;
import nl.gertjanidema.netex.dataload.dto.StNetexAdminZone;
import nl.gertjanidema.netex.dataload.dto.StNetexAdminZoneRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexLineRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexNetwork;
import nl.gertjanidema.netex.dataload.dto.StNetexNetworkRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexPointOnJourneyRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexPointOnRoute;
import nl.gertjanidema.netex.dataload.dto.StNetexPointOnRouteRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexProductCategoryRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexQuayRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexResponsibilitySetRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexRoute;
import nl.gertjanidema.netex.dataload.dto.StNetexRouteRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexScheduledStopPointRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexStopPlaceRepository;
import nl.gertjanidema.netex.dataload.ndov.NdovSession;
import nl.gertjanidema.netex.dataload.processors.StNetexAdminZoneProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexLineProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexNetworkProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexPointOnRouteProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexProductCategoryProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexResponsibilitySetProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexRouteProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexScheduledStopPointProcessor;
import nl.gertjanidema.netex.dataload.processors.StNetexStopPlaceProcessor;

@Component
public class NetexFileProcessor {
    private static Logger LOG = LoggerFactory.getLogger(NetexFileProcessor.class);

//    private PublicationDeliveryStructure delivery;
//    private StNetexDelivery stDelivery;

    @Inject StNetexProductCategoryRepository productCategoryRepository;
    @Inject StNetexResponsibilitySetRepository responsibilitySetRepository;
    @Inject StNetexNetworkRepository networkRepository;
    @Inject StNetexAdminZoneRepository adminZoneRepository;
    @Inject StNetexLineRepository lineRepository;
    @Inject StNetexRouteRepository routeRepository;
    @Inject StNetexPointOnRouteRepository pointOnRouteRepository;
    @Inject StNetexPointOnJourneyRepository pointOnJourneyRepository;
    @Inject StNetexScheduledStopPointRepository scheduledStopPointRepository;
    @Inject StNetexQuayRepository quayRepository;
    @Inject StNetexStopPlaceRepository stopPlaceRepository;
    
    @Inject StNetexStopPlaceProcessor stopPlaceProcessor;
    @Inject StNetexNetworkProcessor networkProcessor;
    @Inject StNetexAdminZoneProcessor adminZoneProcessor;
    @Inject StNetexResponsibilitySetProcessor responsibilitySetProcessor;
    @Inject StNetexProductCategoryProcessor productCategoryProcessor;
    @Inject StNetexScheduledStopPointProcessor scheduledStopPointProcessor;
    @Inject StNetexRouteProcessor routeProcessor;
    @Inject StNetexPointOnRouteProcessor pointOnRouteProcessor;
    @Inject StNetexLineProcessor lineProcessor;
    
    @Transactional
    public void processData(NdovNetexFileInfo fileInfo, NdovSession session) {
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
            delivery.getDataObjects().getCompositeFrameOrCommonFrame().forEach(frameStructure -> {
                if (frameStructure.getDeclaredType().equals(CompositeFrame.class)) {
                    processCompositeFrame((CompositeFrame) frameStructure.getValue(), fileInfo.getFileSetId());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processCompositeFrame(CompositeFrame compositeFrame, String fileSetId) {
        if (compositeFrame.getFrames() == null) return;
        compositeFrame.getFrames().getCommonFrame().forEach(commonFrame -> {
            if (commonFrame.getDeclaredType().equals(ResourceFrame.class)) {
                processResourceFrame((ResourceFrame)commonFrame.getValue(), fileSetId);
            }
            else if (commonFrame.getDeclaredType().equals(ServiceFrame.class)) {
                processServiceFrame((ServiceFrame)commonFrame.getValue(), fileSetId);
            }
            else if (commonFrame.getDeclaredType().equals(SiteFrame.class)) {
                processSiteFrame((SiteFrame)commonFrame.getValue(), fileSetId);
            }
            else if (commonFrame.getDeclaredType().equals(GeneralFrame.class)) {
                processGeneralFrame((GeneralFrame)commonFrame.getValue(), fileSetId);
            }
        });
    }

    private void processGeneralFrame(GeneralFrame frame, String fileSetId) {
        frame.getMembers().getGeneralFrameMemberOrDataManagedObjectOrEntity_Entity().forEach(member -> {
            if (member.getDeclaredType().isAssignableFrom(Network.class)) {
                processNetwork((Network) member.getValue(), fileSetId);
            }
            else if (member.getDeclaredType().isAssignableFrom(TransportAdministrativeZone.class)) {
                processAdminZone((TransportAdministrativeZone) member.getValue(), fileSetId);
            }
        });
    }

    private void processNetwork(Network network, String fileSetId) {
        StNetexNetwork stNetwork;
        try {
            stNetwork = networkProcessor.process(network);
            stNetwork.setFileSetId(fileSetId);
            networkRepository.save(stNetwork);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processAdminZone(TransportAdministrativeZone adminZone, String fileSetId) {
        StNetexAdminZone stAdminZone;
        try {
            stAdminZone = adminZoneProcessor.process(adminZone);
            stAdminZone.setFileSetId(fileSetId);
            adminZoneRepository.save(stAdminZone);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processResourceFrame(ResourceFrame frame, String fileSetId) {
        if (frame.getTypesOfValue() != null) {
            frame.getTypesOfValue().getValueSetOrTypeOfValue().forEach(element -> {
                if (element.getDeclaredType().equals(TypeOfProductCategory.class)) {
                    processProductCategory((TypeOfProductCategory)element.getValue(), fileSetId);
                }
            });
        }
        if (frame.getResponsibilitySets() != null) {
            try {
                for (var responsibilitySet :frame.getResponsibilitySets().getResponsibilitySet()) {
                    if (responsibilitySet.getName() != null) {
                        var netexResponsibilitySet = responsibilitySetProcessor.process(responsibilitySet);
                        netexResponsibilitySet.setFileSetId(fileSetId);
                        responsibilitySetRepository.save(netexResponsibilitySet);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void processProductCategory(TypeOfProductCategory productCategory, String fileSetId) {
        try {
            var netexProductCategory = productCategoryProcessor.process(productCategory);
            netexProductCategory.setFileSetId(fileSetId);
            productCategoryRepository.save(netexProductCategory);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processServiceFrame(ServiceFrame frame, String fileSetId) {
        if (frame.getLines() != null) {
            frame.getLines().getLine_Dummy().stream().map(JAXBElement::getValue).map(Line_VersionStructure.class::cast)
                .forEach(line -> {
                    if (line instanceof Line) {
                        processLine((Line) line, fileSetId);
                    }
                    else LOG.info("Unprocessed line type: {}", line.getClass().getName());
                });
        }
        if (frame.getScheduledStopPoints() != null) {
            frame.getScheduledStopPoints().getScheduledStopPoint().stream()
                .forEach(ssp -> processScheduledStopPoint(ssp, fileSetId));
        }
        if (frame.getRoutes() != null) {
            frame.getRoutes().getRoute_Dummy().stream().map(JAXBElement::getValue).map(LinkSequence_VersionStructure.class::cast)
                .forEach(linkSequence -> {
                    if (linkSequence instanceof Route) {
                        processRoute((Route) linkSequence, fileSetId);
                    }
                    else LOG.info("Unprocessed route type: {}", linkSequence.getClass().getName());
                });
        }
    }

    private void processScheduledStopPoint(ScheduledStopPoint stopPoint, String fileSetId) {
        try {
            var netexStopPoint = scheduledStopPointProcessor.process(stopPoint);
            netexStopPoint.setFileSetId(fileSetId);
            scheduledStopPointRepository.save(netexStopPoint);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processRoute(Route route, String fileSetId) {
        try {
            var netexRoute = routeProcessor.process(route);
            netexRoute.setFileSetId(fileSetId);
            routeRepository.save(netexRoute);
            processPointOnRoute(route, netexRoute, fileSetId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processPointOnRoute(Route route, StNetexRoute netexRoute, String fileSetId) {
        try {
            var pointsInSequence = route.getPointsInSequence();
            if (pointsInSequence != null) {
              var pointsOnRoute = route.getPointsInSequence().getPointOnRoute();
              var stPoints = new ArrayList<StNetexPointOnRoute>(pointsOnRoute.size());
              pointsOnRoute.forEach(por -> {
                StNetexPointOnRoute nPor;
                try {
                    nPor = pointOnRouteProcessor.process(por);
                    nPor.setFileSetId(fileSetId);
                    nPor.setNetexRouteId(netexRoute.getNetexId());
                    stPoints.add(nPor);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
              });
            pointOnRouteRepository.saveAll(stPoints);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processLine(Line line, String fileSetId) {
        try {
            var netexLine = lineProcessor.process(line);
            netexLine.setFileSetId(fileSetId);
            lineRepository.save(netexLine);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processSiteFrame(SiteFrame frame, String fileSetId) {
        if (frame.getStopPlaces() != null) {
            frame.getStopPlaces().getStopPlace().stream()
                .map(StopPlace.class::cast)
                .forEach(sp -> processStopPlace(sp, fileSetId));
        }
    }

    private void processStopPlace(StopPlace stopPlace, String fileSetId) {
        try {
            var netexStopPlace = stopPlaceProcessor.process(stopPlace);
            netexStopPlace.setFileSetId(fileSetId);
            netexStopPlace.getQuays().forEach(quay -> {
                quay.setFileSetId(fileSetId);
            });
            stopPlaceRepository.save(netexStopPlace);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
