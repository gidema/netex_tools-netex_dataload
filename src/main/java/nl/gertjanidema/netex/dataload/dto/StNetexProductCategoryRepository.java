package nl.gertjanidema.netex.dataload.dto;

import org.springframework.data.repository.CrudRepository;

public interface StNetexProductCategoryRepository extends CrudRepository<StNetexProductCategory, String> {

    public void deleteByFileSetId(String fileSetId);
}

