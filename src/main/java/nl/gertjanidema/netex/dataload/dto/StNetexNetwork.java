package nl.gertjanidema.netex.dataload.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@BatchSize(size=2)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexNetwork {
    final static String SEQ = "st_netex_network_id_seq";
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(schema="netex", name=SEQ, sequenceName = SEQ, allocationSize=2)
    private Long id;
    @OneToOne(mappedBy = "network")
    @JoinColumn(name = "frame_id")
    private StNetexGeneralFrame frame;
    @Include
    private String netexId;
    @Include
    private String version;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String name;
    private String shortName;
    private String description;
    private String groupOfLinesType;
    private String authorityRef;
    private String fileSetId;
}
