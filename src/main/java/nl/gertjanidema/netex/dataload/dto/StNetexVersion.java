package nl.gertjanidema.netex.dataload.dto;

import java.time.LocalDateTime;

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
@BatchSize(size=10)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexVersion {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="st_netex_version_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_version_id_seq", sequenceName = "st_netex_version_id_seq", allocationSize=10)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "frame")
    private StNetexCompositeFrame frame;
    @Include
    private String netexId;
    private String modification;
    private String version;
    private String versionType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String fileSetId;
}
