package nl.gertjanidema.netex.dto;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface NetexNetworkRepository extends CrudRepository<NetexNetwork, String> {

    @Modifying
    @Query(
            value = """
TRUNCATE TABLE netex.netex_network CASCADE;
INSERT INTO netex.netex_network (netex_id, from_date, to_date, name, short_name, description, group_of_lines_type,
        authority_ref, file_set_id, administrative_zone)
    SELECT netex_id,
      from_date,
      to_date, name,
      short_name,
      description,
      group_of_lines_type,
      authority_ref,
      file_set_id,
      'NL:DOVA:TransportAdministrativeZone:' || short_name AS administrative_zone
    FROM netex.st_netex_network
    WHERE netex_id LIKE 'NL:%' AND file_set_id = 'DOVA_networks';
""",
            nativeQuery = true
    )
    @Transactional
    public void populate();
}

