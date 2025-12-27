package nl.gertjanidema.netex.dataload.dto;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@BatchSize(size=100)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexScheduledStopPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Include
    private String netexId;
    @Include
    private String version;
    private String stopAreaId;
    private List<String> tariffZones;
    private String name;
    private String shortName;
    private String place;
    private String routePointRef;
    private Double xCoordinate;
    private Double yCoordinate;
    private String userStopCode;
    private String userStopOwnerCode;
    private Boolean forBoarding;
    private Boolean forAlighting;
    private String fileSetId;
}
