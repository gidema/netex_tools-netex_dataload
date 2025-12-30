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
public class StNetexServiceFrame {
    final static String SEQ = "st_netex_service_frame_id_seq";
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator=SEQ)
    @SequenceGenerator(schema="netex", name=SEQ, sequenceName = SEQ, allocationSize=10)
    private Long id;
    @ToString.Include
    private String ref;
    @ToString.Include
    private String refVersion;
    @OneToOne(mappedBy = "serviceFrame")
    @JoinColumn(name="parent_id")
    private StNetexCompositeFrame parentFrame;
    @OneToMany(mappedBy="frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexScheduledStopPoint> scheduledStopPoints;
    @OneToMany(mappedBy="frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexLine> lines;
    @OneToMany(mappedBy="frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StNetexRoute> routes;
}
