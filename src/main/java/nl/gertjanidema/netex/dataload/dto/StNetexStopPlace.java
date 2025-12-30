package nl.gertjanidema.netex.dataload.dto;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "netex")
@BatchSize(size=500)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexStopPlace {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="st_netex_stop_place_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_stop_place_id_seq",sequenceName="st_netex_stop_place_id_seq", allocationSize=500)
    private Long id;
    @ManyToOne()
    @JoinColumn(name="frame_id")
    private StNetexSiteFrame frame;
    @Include
    private String netexId;
    @Include
    private String version;
    private String name;
    private String privateCode;
    private Double x;
    private Double y;
    private String fileSetId;
    @OneToMany(mappedBy = "stopPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexQuay> quays;
}
