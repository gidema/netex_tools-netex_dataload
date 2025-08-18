package nl.gertjanidema.netex.dataload.dto;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@Getter
@Setter
public class StNetexResponsibilitySet {
    @Id
    private String id;
    private String name;
    private Set<String> roles;
    private String fileSetId;
}
