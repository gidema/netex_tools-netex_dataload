package nl.gertjanidema.netex.dataload.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StNetexRouteRepository extends JpaRepository<StNetexRoute, Long> {

    // public void deleteByFileSetId(String fileSetId);
}

