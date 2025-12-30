package nl.gertjanidema.netex.dataload.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class StNetexGeneralFrame {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="st_netex_general_frame_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_general_frame_id_seq", sequenceName = "st_netex_general_frame_id_seq", allocationSize=10)
    private Long id;
    @ToString.Include
    private String ref;
    @ToString.Include
    private String refVersion;
    @OneToOne(mappedBy = "generalFrame")
    @JoinColumn(name="parent_id")
    private StNetexCompositeFrame parentFrame;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "network_id", referencedColumnName = "id")
    private StNetexNetwork network;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "admin_zone_id", referencedColumnName = "id")
    private StNetexAdminZone administrativeZone;
}
