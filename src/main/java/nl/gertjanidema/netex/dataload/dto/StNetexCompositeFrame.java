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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(schema="netex")
@BatchSize(size = 10)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class StNetexCompositeFrame {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="st_netex_composite_frame_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_composite_frame_id_seq", sequenceName = "st_netex_composite_frame_id_seq", allocationSize=10)
    private Long id;
    @ToString.Include
    private String ref;
    @ToString.Include
    private String refVersion;
    @Include
    @ManyToOne()
    @JoinColumn(name = "delivery_id")
    private StNetexDelivery delivery;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resource_frame_id", referencedColumnName = "id")
    private StNetexResourceFrame resourceFrame;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "service_frame_id", referencedColumnName = "id")
    private StNetexServiceFrame serviceFrame;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "site_frame_id", referencedColumnName = "id")
    private StNetexSiteFrame siteFrame;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "general_frame_id", referencedColumnName = "id")
    private StNetexGeneralFrame generalFrame;
    @OneToMany(mappedBy = "frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexVersion> versions;
}
