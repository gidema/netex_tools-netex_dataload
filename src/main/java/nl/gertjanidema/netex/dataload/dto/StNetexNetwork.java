package nl.gertjanidema.netex.dataload.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
public class StNetexNetwork {
    @Id
    private String id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String name;
    private String shortName;
    private String description;
    private String groupOfLinesType;
    private String authorityRef;
    private String fileSetId;
}
