package org.cbioportal.G2Smutation.web.database;

import java.util.List;

import org.cbioportal.G2Smutation.web.database.rs_mutation_entry;
import org.springframework.data.jpa.repository.JpaRepository;


public interface rsRepository extends JpaRepository<rs_mutation_entry, Integer> {
	List<rs_mutation_entry> findByrsSnpIdBetween(Integer id1,Integer id2);
	List<rs_mutation_entry> findByrsIdBetween(Integer id1,Integer id2);
}