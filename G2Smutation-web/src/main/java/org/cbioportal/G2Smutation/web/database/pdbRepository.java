package org.cbioportal.G2Smutation.web.database;

import org.cbioportal.G2Smutation.web.database.pdb_seq_alignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
//CRUD refers Create, Read, Update, Delete
public interface pdbRepository extends JpaRepository<pdb_seq_alignment, Integer> {
	List<pdb_seq_alignment> findByalignmentId(Integer id);
}
