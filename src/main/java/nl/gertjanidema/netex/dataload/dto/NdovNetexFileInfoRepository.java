package nl.gertjanidema.netex.dataload.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NdovNetexFileInfoRepository extends JpaRepository<NdovNetexFileInfo, String> {

    Optional<NdovNetexFileInfo> findByFileName(String fileName);

    @Query(value = """
SELECT fi
FROM NdovNetexFileInfo fi
JOIN StNetexDelivery dlv ON dlv.fileInfo = fi
WHERE fi.fileSetId = :fileSetId
""")
    Optional<NdovNetexFileInfo> findCurrentByFileSetId(@Param("fileSetId") String fileSetId);
}

