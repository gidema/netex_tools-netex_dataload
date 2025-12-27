package nl.gertjanidema.netex.dataload.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StNetexDeliveryRepository extends JpaRepository<StNetexDelivery, String> {
    Optional<StNetexDelivery> findByFileSetId(String fileSetId);
}

