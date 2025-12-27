package nl.gertjanidema.netex.dataload.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
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
public class StNetexDelivery {
    @Id
    @Include
    private String fileSetId;
    private String filename;
    private LocalDateTime publicationTimestamp;
    private OffsetDateTime downloadTimestamp;
    private String participantRef;
    private String description;
    private LocalDateTime baselineStartDate;
    private LocalDateTime baselineEndDate;
}
