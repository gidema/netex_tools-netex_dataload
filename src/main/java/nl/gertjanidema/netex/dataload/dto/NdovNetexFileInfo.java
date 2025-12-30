package nl.gertjanidema.netex.dataload.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(schema="netex")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NdovNetexFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Include
    private Long id;
    private String ndovSourceId;
    private String fileSetId;
    private Boolean isCurrent;
    @Include
    private String directory;
    @Include
    private String fileName;
    private Instant lastModified;
    private LocalDateTime publicationTimestamp;
    private String participentRef;
    private String description;
    private Long size;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDate versionDate;
    private String version;
    private Instant importedAt;
    private Instant discardedAt;
    private String frameId;
}
