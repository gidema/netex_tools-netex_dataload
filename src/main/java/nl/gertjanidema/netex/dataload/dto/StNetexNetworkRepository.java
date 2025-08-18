package nl.gertjanidema.netex.dataload.dto;

import org.springframework.data.repository.CrudRepository;

public interface StNetexNetworkRepository extends CrudRepository<StNetexNetwork, String> {

    public void deleteByFileSetId(String fileSetId);
}

