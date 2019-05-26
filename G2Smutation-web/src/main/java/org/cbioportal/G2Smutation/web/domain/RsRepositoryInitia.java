package org.cbioportal.G2Smutation.web.domain;

import java.util.List;

import org.cbioportal.G2Smutation.web.models.db.rs_mutation_entry_Initia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RsRepositoryInitia extends JpaRepository<rs_mutation_entry_Initia, Integer> {
	List<rs_mutation_entry_Initia> findTop20ByrsIdAfter(Integer id);
}
