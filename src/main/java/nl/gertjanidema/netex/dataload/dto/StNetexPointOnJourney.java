package nl.gertjanidema.netex.dataload.dto;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="netex")
@BatchSize(size=100)
@Getter
@Setter
public class StNetexPointOnJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Include
    private String netexId;
    @Include
    private String version;
    private String journeyId;
    private String routeId;
    private Integer sequence;
    private String routePointRef;
    private String pointType;
    private String fileSetId;
}
