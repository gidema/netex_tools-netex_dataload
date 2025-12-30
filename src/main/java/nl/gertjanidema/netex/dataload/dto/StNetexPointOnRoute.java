package nl.gertjanidema.netex.dataload.dto;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="netex")
@BatchSize(size=100)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexPointOnRoute {
    final static String SEQ = "st_netex_point_on_route_id_seq";
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(schema="netex", name=SEQ, sequenceName = SEQ, allocationSize=100)
    private Long id;
    @Include
    private String netexId;
    @Include
    private String netexRouteId;
    private String version;
    private String routeId;
    private Integer sequence;
    private String routePointRef;
    private String fileSetId;
}
