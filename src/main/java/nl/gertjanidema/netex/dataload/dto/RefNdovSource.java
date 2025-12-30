package nl.gertjanidema.netex.dataload.dto;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="netex")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RefNdovSource {
    @Id
    // We use IDENTITY here, so we can easily import from csv. No need for SEQUENCE, as we don't import bulk.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique=true)
    @EqualsAndHashCode.Include
    private String sourceName;
    private Boolean ignore;
    private Boolean global;
    private String pattern;
    @OneToMany(mappedBy = "source", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<RefNdovSourceField> fields;
}
