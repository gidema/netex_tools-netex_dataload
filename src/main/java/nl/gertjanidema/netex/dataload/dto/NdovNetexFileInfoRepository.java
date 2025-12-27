package nl.gertjanidema.netex.dataload.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NdovNetexFileInfoRepository extends JpaRepository<NdovNetexFileInfo, String> {

    Optional<NdovNetexFileInfo> findByFileName(String fileName);
}

