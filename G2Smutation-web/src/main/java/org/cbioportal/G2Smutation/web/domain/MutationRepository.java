package org.cbioportal.G2Smutation.web.domain;

import org.cbioportal.G2Smutation.web.models.db.MutationUsageTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface MutationRepository extends JpaRepository<MutationUsageTable, Integer> {
	List<MutationUsageTable> findTop10BymutationIdGreaterThan(Integer id);
	
	List<MutationUsageTable> findByseqNameContaining(String id);
	
	List<MutationUsageTable> findByseqIndex(String id);
	List<MutationUsageTable> findBypdbIndex(String id);
	List<MutationUsageTable> findByseqIndexAndPdbIndex(String id1,String id2);
	
	List<MutationUsageTable> findByseqResidue(String id);
	List<MutationUsageTable> findBypdbResidue(String id);
	List<MutationUsageTable> findByseqResidueAndPdbResidue(String id1,String id2);
	
	List<MutationUsageTable> findBypdbNoStartingWith(String id);
	
	
	@Query(value = "select * from mutation_usage_table where PDB_NO LIKE '?1%' And SEQ_INDEX = ?2", nativeQuery = true)
	List<MutationUsageTable> findBypdbNoAndPdbIndex(String id1,Integer id2);
	List<MutationUsageTable> findByalignmentId(Integer id);
}
