package nl.gertjanidema.netex.dataload.dto;

import java.util.List;

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
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexSiteFrame {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="st_netex_site_frame_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_site_frame_id_seq", sequenceName = "st_netex_site_frame_id_seq", allocationSize=10)
    private Long id;
    @ToString.Include
    private String ref;
    @ToString.Include
    private String refVersion;
    @OneToOne(mappedBy = "siteFrame")
    @JoinColumn(name="parent_id")
    private StNetexCompositeFrame parentFrame;
    @OneToMany(mappedBy="frame", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StNetexStopPlace> stopPlaces;
}
