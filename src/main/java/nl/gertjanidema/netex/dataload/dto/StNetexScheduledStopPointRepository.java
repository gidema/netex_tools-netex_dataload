package nl.gertjanidema.netex.dataload.dto;

import org.springframework.data.repository.CrudRepository;

public interface StNetexScheduledStopPointRepository extends CrudRepository<StNetexScheduledStopPoint, String> {

    public void deleteByFileSetId(String fileSetId);
}

