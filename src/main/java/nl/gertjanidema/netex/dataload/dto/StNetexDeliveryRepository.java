package nl.gertjanidema.netex.dataload.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StNetexDeliveryRepository extends JpaRepository<StNetexDelivery, String> {
    @Query(value = """
SELECT dlv
FROM StNetexDelivery dlv
JOIN NdovNetexFileInfo fi ON dlv.fileInfo = fi
WHERE fi.fileSetId = :fileSetId
""")
    Optional<StNetexDelivery> findByFileSetId(@Param("fileSetId") String fileSetId);
}

