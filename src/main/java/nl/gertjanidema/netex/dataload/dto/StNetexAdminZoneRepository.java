package nl.gertjanidema.netex.dataload.dto;

import org.springframework.data.repository.CrudRepository;

public interface StNetexAdminZoneRepository extends CrudRepository<StNetexAdminZone, String> {

    public void deleteByFileSetId(String fileSetId);
}

