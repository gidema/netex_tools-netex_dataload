package nl.gertjanidema.netex.dataload.dto;

import java.time.LocalDateTime;

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
@BatchSize(size=50)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
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
