package nl.gertjanidema.netex.dataload.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rutebanken.netex.model.PointProjection;
import org.rutebanken.netex.model.ScheduledStopPoint;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexScheduledStopPoint;

@Component
public class StNetexScheduledStopPointProcessor extends AbstractItemProcessor implements ItemProcessor<ScheduledStopPoint, StNetexScheduledStopPoint> {
 
    @Override
    public StNetexScheduledStopPoint process(ScheduledStopPoint stop) throws Exception {
        var stopPoint = new StNetexScheduledStopPoint();
        stopPoint.setNetexId(stop.getId());
        stopPoint.setVersion(stop.getVersion());
        var stopName = getStopName(stop);
        stopPoint.setName(stopName.name());
        stopPoint.setPlace(stopName.place());
        if (stop.getShortName() != null) {
            stopPoint.setShortName((String) stop.getShortName().getContent().get(0));
        }
        var idParts = stop.getId().split(":");

        if (stop.getPrivateCode() != null && "UserStopCode".equals(stop.getPrivateCode().getType())) {
            stopPoint.setUserStopCode(stop.getPrivateCode().getValue());
        }
        else if (stop.getPrivateCodes() != null) {
            stop.getPrivateCodes().getPrivateCode().forEach(code -> {
                if ("UserStopCode".equals(code.getType())) {
                    stopPoint.setUserStopCode(code.getValue());
                }
            });
        }
//        if (stopPoint.getUserStopCode() == null) {
//            stopPoint.setUserStopCode(getUserStopCodeFromId(idParts));
//        }
        stopPoint.setUserStopOwnerCode(getOwnerCodeFromId(idParts));

        stopPoint.setXCoordinate(getX(stop.getLocation()));
        stopPoint.setYCoordinate(getY(stop.getLocation()));
        stopPoint.setRoutePointRef(getRoutePointRef(stop));
        stopPoint.setTariffZones(getTariffZones(stop));
        stopPoint.setForBoarding(stop.isForBoarding() == null ? true : stop.isForBoarding()); 
        stopPoint.setForAlighting(stop.isForAlighting() == null ? true : stop.isForAlighting());
        return stopPoint;
    }
    
    private static StopName getStopName(ScheduledStopPoint stop) {
        if (stop.getName() == null) return null;
        var parts = toString(stop.getName()).split(", ", 2);
        if (parts.length ==2) {
            return new StopName(parts[1], parts[0]);
        }
        return new StopName(parts[0], null);                
    }
    
    private static String getRoutePointRef(ScheduledStopPoint stop) {
        if (stop.getProjections() != null) {
            for (var jaxbElement : stop.getProjections().getProjectionRefOrProjection()) {
                if (jaxbElement.getDeclaredType().equals(PointProjection.class)) {
                    var pointProjection = (PointProjection)jaxbElement.getValue();
                    var ptpRef = pointProjection.getProjectToPointRef();
                    return (ptpRef == null ? null : ptpRef.getRef());
                }
            }
        }
        return null;
    }
    
    private static List<String> getTariffZones(ScheduledStopPoint stop) {
        if(stop.getTariffZones() == null) return Collections.emptyList(); 
        var zoneRefs = stop.getTariffZones().getTariffZoneRef_Dummy();
        List<String> zones = new ArrayList<>(zoneRefs.size());
        zoneRefs.forEach(z -> {
            var zoneRef = z.getValue();
            String[] parts = zoneRef.getRef().split(":");
            if (parts.length == 3 && "DOVA".equals(parts[0])) {
                zones.add(parts[2]);
            }
        });
        return zones;
    }
    
    private static String getOwnerCodeFromId(String[] idParts) {
        return "NL".equals(idParts[0]) ? idParts[1] : idParts[0];
    }
    
//    private static String getUserStopCodeFromId(String[] idParts) {
//        return idParts[idParts.length -1];
//    }
    
    static record StopName(String name, String place) {}
}