package nl.gertjanidema.netex.dataload.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="netex")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StNetexDelivery {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="st_netex_delivery_id_seq")
    @SequenceGenerator(schema="netex", name="st_netex_delivery_id_seq", sequenceName = "st_netex_delivery_id_seq", allocationSize=10)
    private Long id;
    @Include
    @OneToOne()
    @JoinColumn(name = "file_info_id")
    private NdovNetexFileInfo fileInfo;
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexCompositeFrame> compositeFrames;
    private LocalDateTime publicationTimestamp;
    private OffsetDateTime downloadTimestamp;
    private String participantRef;
    private String description;
    private LocalDateTime baselineStartDate;
    private LocalDateTime baselineEndDate;
}
