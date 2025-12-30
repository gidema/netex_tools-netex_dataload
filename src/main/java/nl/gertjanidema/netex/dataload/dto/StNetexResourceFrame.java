package nl.gertjanidema.netex.dataload.dto;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(schema="netex")
@BatchSize(size=10)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexResourceFrame {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="st_netex_resource_frame_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_resource_frame_id_seq", sequenceName = "st_netex_resource_frame_id_seq", allocationSize=10)
    private Long id;
    @ToString.Include
    private String ref;
    @ToString.Include
    private String refVersion;
    @OneToOne(mappedBy = "resourceFrame")
    @JoinColumn(name="parent_id")
    private StNetexCompositeFrame parentFrame;
    @OneToMany(mappedBy="frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexProductCategory> productCategories;
    @OneToMany(mappedBy="frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexResponsibilitySet> responsibilitySets;
}
