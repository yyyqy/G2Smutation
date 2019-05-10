package org.cbioportal.G2Smutation.web.domain;

import org.cbioportal.G2Smutation.web.database.mutation_usage_table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface MutationRepository extends JpaRepository<mutation_usage_table, Integer> {
	
	List<mutation_usage_table> findTop20BymutationIdGreaterThan(Integer id);
	List<mutation_usage_table> findByseqId(Integer id);
	List<mutation_usage_table> findByseqIndex(Integer id);
	List<mutation_usage_table> findByseqIdAndSeqIndex(Integer id1,Integer id2);
	List<mutation_usage_table> findBypdbNoStartingWith(String id);
	
	@Query(value = "select * from mutation_usage_table where PDB_NO LIKE '?1%' And SEQ_INDEX = ?2", nativeQuery = true)
	List<mutation_usage_table> findBypdbNoAndPdbIndex(String id1,Integer id2);
	
	/* 3*3 */
	List<mutation_usage_table> findBySeqResidueOrderByPdbIndex(String id);
	List<mutation_usage_table> findBypdbIndex(Integer id);
	List<mutation_usage_table> findBypdbResidueOrderByPdbIndex(String id);
	
	List<mutation_usage_table> findBypdbIndexAndPdbResidue(Integer id1,String id2);
	List<mutation_usage_table> findByseqResidueAndPdbResidue(String id1,String id2);
	List<mutation_usage_table> findBypdbIndexAndSeqResidue(Integer id1,String id2);
    
	List<mutation_usage_table> findBySeqResidueAndPdbResidueAndPdbIndex(String id1,String id2,Integer id3);
}
