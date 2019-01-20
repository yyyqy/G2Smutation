package org.cbioportal.G2Smutation.web.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface rsRepositoryInitia extends JpaRepository<rs_mutation_entry_Initia, Integer> {
	List<rs_mutation_entry_Initia> findTop20ByrsIdAfter(Integer id);
}