package nl.gertjanidema.netex.dataload.dto;

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
public class RefNdovIgnoreFileset {
    @Id
    @Include
    private String fileSetId;
}
