package nl.gertjanidema.netex.dataload.dto;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="st_netex_sch_stop_point_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_sch_stop_point_id_seq",sequenceName="st_netex_sch_stop_point_id_seq", allocationSize=100)
    private Long id;
    @ManyToOne()
    @JoinColumn(name="frame_id")
    private StNetexServiceFrame frame;
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
