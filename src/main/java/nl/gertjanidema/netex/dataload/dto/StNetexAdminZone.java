package nl.gertjanidema.netex.dataload.dto;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@BatchSize(size=50)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexAdminZone {
    final static String SEQ = "st_netex_admin_zone_id_seq";
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(schema="netex", name=SEQ, sequenceName = SEQ, allocationSize=100)
    private Long id;
    @OneToOne(mappedBy = "administrativeZone")
    private StNetexGeneralFrame frame;
    @Include
    private String netexId;
    @Include
    private String version;
    private String name;
    private String shortName;
    private String description;
    private String fileSetId;
}
