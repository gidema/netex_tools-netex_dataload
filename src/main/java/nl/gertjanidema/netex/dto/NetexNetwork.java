package nl.gertjanidema.netex.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NetexNetwork {
    @Id
    @Include
    private String netexId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String name;
    private String shortName;
    private String description;
    private String groupOfLinesType;
    private String authorityRef;
    private String fileSetId;
}
