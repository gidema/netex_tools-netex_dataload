package nl.gertjanidema.netex.dataload.dto;

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
public class StNetexLine {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="st_netex_line_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_line_id_seq",sequenceName="st_netex_line_id_seq", allocationSize=100)
    private Long id;
    @ManyToOne()
    @JoinColumn(name="frame_id")
    private StNetexServiceFrame frame;
    @Include
    private String netexId;
    @Include
    private String version;
    private String responsibilitySetRef;
    private String name;
    private String brandingRef;
    private String directionType;
    private String productCategoryRef;
    private String transportMode;
    private String publicCode;
    private String privateCode;
    private String colour;
    private String textColour;
    private boolean mobilityImpairedAccess;
    private String fileSetId;
}
